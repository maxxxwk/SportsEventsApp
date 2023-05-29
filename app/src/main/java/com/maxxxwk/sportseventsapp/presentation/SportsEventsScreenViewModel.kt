package com.maxxxwk.sportseventsapp.presentation

import androidx.lifecycle.viewModelScope
import com.maxxxwk.sportseventsapp.R
import com.maxxxwk.sportseventsapp.core.uitext.UIText
import com.maxxxwk.sportseventsapp.core.viewmodel.BaseViewModel
import com.maxxxwk.sportseventsapp.domain.SportsEventsRepository
import com.maxxxwk.sportseventsapp.presentation.models.SportEventsUIMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SportsEventsScreenViewModel @Inject constructor(
    private val sportsEventsRepository: SportsEventsRepository,
    private val sportEventsUIMapper: SportEventsUIMapper
) : BaseViewModel<SportsEventsScreenState, SportsEventsScreenIntent>(SportsEventsScreenState.Loading) {

    private val hiddenSportsIndexes = MutableStateFlow(emptySet<Int>())

    init {
        combine(
            sportsEventsRepository.getSportsEventsFlow(),
            hiddenSportsIndexes
        ) { result, indexes ->
            result.map { sportEventsUIMapper.mapSportsModels(it, indexes) }
        }.onEach { result ->
            result.getOrNull()?.let {
                intents.send(SportsEventsScreenIntent.ShowSportsEvents(it))
            }
            result.exceptionOrNull()?.let { throwable ->
                intents.send(SportsEventsScreenIntent.ShowError(throwable.getErrorMessage()))
            }
        }.launchIn(viewModelScope)
    }

    private fun Throwable.getErrorMessage(): UIText {
        return this.message?.let {
            UIText.DynamicString(it)
        } ?: UIText.StringResource(R.string.unknown_error)
    }

    override fun reduce(intent: SportsEventsScreenIntent): SportsEventsScreenState = when (intent) {
        SportsEventsScreenIntent.RetryLoading -> {
            viewModelScope.launch { sportsEventsRepository.startRetryLoading() }
            SportsEventsScreenState.Loading
        }
        is SportsEventsScreenIntent.ShowError -> SportsEventsScreenState.Error(intent.errorMessage)
        is SportsEventsScreenIntent.ShowSportsEvents -> SportsEventsScreenState.Content(intent.sports)
        is SportsEventsScreenIntent.ChangeIsFavorite -> {
            viewModelScope.launch { sportsEventsRepository.changeFavoriteMark(intent.id) }
            state.value
        }
    }

    fun retryLoading() {
        viewModelScope.launch {
            intents.send(SportsEventsScreenIntent.RetryLoading)
        }
    }

    fun changeIsFavorite(id: String) {
        viewModelScope.launch {
            intents.send(SportsEventsScreenIntent.ChangeIsFavorite(id))
        }
    }

    fun hideEvents(index: Int) {
        viewModelScope.launch {
            hiddenSportsIndexes.update {
                hashSetOf<Int>().apply {
                    addAll(it)
                    if (!add(index)) {
                        remove(index)
                    }
                }
            }
        }
    }
}
