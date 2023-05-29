package com.maxxxwk.sportseventsapp.presentation

import com.maxxxwk.sportseventsapp.core.uitext.UIText
import com.maxxxwk.sportseventsapp.presentation.models.SportUIModel

sealed interface SportsEventsScreenIntent {
    data class ShowSportsEvents(val sports: List<SportUIModel>) : SportsEventsScreenIntent
    data class ShowError(val errorMessage: UIText) : SportsEventsScreenIntent
    object RetryLoading : SportsEventsScreenIntent
    data class ChangeIsFavorite(val id: String) : SportsEventsScreenIntent
}
