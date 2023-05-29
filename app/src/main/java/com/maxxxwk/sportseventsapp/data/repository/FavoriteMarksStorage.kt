package com.maxxxwk.sportseventsapp.data.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteMarksStorage {
    suspend fun changeFavoriteMark(id: String)
    val favoritesMarksFlow: Flow<Set<String>>
}
