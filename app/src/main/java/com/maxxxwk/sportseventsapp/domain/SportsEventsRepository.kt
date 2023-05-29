package com.maxxxwk.sportseventsapp.domain

import com.maxxxwk.sportseventsapp.domain.models.Sport
import kotlinx.coroutines.flow.Flow

interface SportsEventsRepository {
    fun getSportsEventsFlow(): Flow<Result<List<Sport>>>
    suspend fun changeFavoriteMark(id: String): Result<Unit>
    suspend fun startRetryLoading()
}
