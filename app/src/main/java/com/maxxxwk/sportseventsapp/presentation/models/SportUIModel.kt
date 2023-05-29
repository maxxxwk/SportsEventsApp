package com.maxxxwk.sportseventsapp.presentation.models

data class SportUIModel(
    val name: String,
    val iconURL: String,
    val events: List<SportEventUIModel>,
    val isExpanded: Boolean
)
