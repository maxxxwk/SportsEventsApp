package com.maxxxwk.sportseventsapp.presentation.models

data class SportEventUIModel(
    val id: String,
    val name: String,
    val timeToStart: String,
    val isFavorite: Boolean
)
