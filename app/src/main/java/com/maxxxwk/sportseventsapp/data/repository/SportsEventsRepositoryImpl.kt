package com.maxxxwk.sportseventsapp.data.repository

import com.maxxxwk.sportseventsapp.core.coroutines.DispatchersProvider
import com.maxxxwk.sportseventsapp.data.models.SportResponse
import com.maxxxwk.sportseventsapp.data.sources.remote.ApiService
import com.maxxxwk.sportseventsapp.domain.SportsEventsRepository
import com.maxxxwk.sportseventsapp.domain.models.Sport
import com.maxxxwk.sportseventsapp.domain.models.SportEvent
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.TimeUnit

@Singleton
class SportsEventsRepositoryImpl @Inject constructor(
    private val favoriteMarksStorage: FavoriteMarksStorage,
    private val apiService: ApiService,
    private val dispatchersProvider: DispatchersProvider
) : SportsEventsRepository {

    private val retryFlow = MutableSharedFlow<Unit>()

    override fun getSportsEventsFlow(): Flow<Result<List<Sport>>> = combine(
        getSportsEvents(),
        favoriteMarksStorage.favoritesMarksFlow
    ) { sportsResult, favoriteEventsIds ->
        sportsResult.map { sports ->
            sports.map { sport ->
                Sport(
                    name = sport.name,
                    id = sport.id,
                    events = sport.events.map {
                        SportEvent(
                            id = it.id,
                            name = it.name,
                            timeToStart = formatTimeToStart(it.timeToStart),
                            isFavorite = favoriteEventsIds.contains(it.id)
                        )
                    }
                )
            }
        }
    }


    override suspend fun changeFavoriteMark(
        id: String
    ): Result<Unit> = withContext(dispatchersProvider.io) {
        runCatching { favoriteMarksStorage.changeFavoriteMark(id) }
    }

    override suspend fun startRetryLoading(): Unit = withContext(dispatchersProvider.io) {
        retryFlow.emit(Unit)
    }

    @Suppress("MagicNumber")
    private fun formatTimeToStart(timeToStart: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeToStart.absoluteValue)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeToStart.absoluteValue) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeToStart.absoluteValue) % 60
        return buildString {
            if (timeToStart < 0) {
                append("-")
            }
            append("$hours:")
            append(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getSportsEvents(): Flow<Result<List<SportResponse>>> = retryFlow
        .onStart { emit(Unit) }
        .map { runCatching { apiService.getSportsEvents() } }
        .flatMapConcat { result ->
            flow {
                val requestTime = System.currentTimeMillis()
                if (result.isFailure) emit(result)
                while (currentCoroutineContext().isActive && result.isSuccess) {
                    delay(1.seconds)
                    emit(
                        result.map { sports ->
                            sports.map { sport ->
                                sport.copy(
                                    events = sport.events.map {
                                        it.copy(
                                            timeToStart = (it.timeToStart + requestTime) - System.currentTimeMillis()
                                        )
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
}
