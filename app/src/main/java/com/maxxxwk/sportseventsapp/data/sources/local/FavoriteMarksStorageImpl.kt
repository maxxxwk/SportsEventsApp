package com.maxxxwk.sportseventsapp.data.sources.local

import com.maxxxwk.sportseventsapp.core.coroutines.DispatchersProvider
import com.maxxxwk.sportseventsapp.data.repository.FavoriteMarksStorage
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@Singleton
class FavoriteMarksStorageImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) : FavoriteMarksStorage {
    private val _favoritesMarksFlow = MutableStateFlow<Set<String>>(emptySet())
    override val favoritesMarksFlow: Flow<Set<String>>
        get() = _favoritesMarksFlow

    override suspend fun changeFavoriteMark(id: String) = withContext(dispatchersProvider.io) {
        _favoritesMarksFlow.update {
            hashSetOf<String>().apply {
                addAll(it)
                if (!add(id)) { remove(id) }
            }
        }
    }
}
