package com.maxxxwk.sportseventsapp.domain.models

data class Sport(
    val name: String,
    val id: String,
    val events: List<SportEvent>
)
