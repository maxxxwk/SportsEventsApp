package com.maxxxwk.sportseventsapp.domain.models

data class SportEvent(
    val id: String,
    val name: String,
    val timeToStart: String,
    val isFavorite: Boolean
)
