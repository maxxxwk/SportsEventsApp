package com.maxxxwk.sportseventsapp.presentation

import com.maxxxwk.sportseventsapp.core.uitext.UIText
import com.maxxxwk.sportseventsapp.presentation.models.SportUIModel

sealed interface SportsEventsScreenState {
    object Loading : SportsEventsScreenState
    data class Error(val message: UIText) : SportsEventsScreenState
    data class Content(val sportsEvents: List<SportUIModel>) : SportsEventsScreenState
}
