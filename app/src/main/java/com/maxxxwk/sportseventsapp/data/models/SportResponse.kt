package com.maxxxwk.sportseventsapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SportResponse(
    @SerialName("i") val id: String,
    @SerialName("d") val name: String,
    @SerialName("e") val events: List<SportResponseEvent>
)
