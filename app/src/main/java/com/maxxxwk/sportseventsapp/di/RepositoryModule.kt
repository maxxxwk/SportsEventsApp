package com.maxxxwk.sportseventsapp.di

import com.maxxxwk.sportseventsapp.data.repository.FavoriteMarksStorage
import com.maxxxwk.sportseventsapp.data.sources.local.FavoriteMarksStorageImpl
import com.maxxxwk.sportseventsapp.data.repository.SportsEventsRepositoryImpl
import com.maxxxwk.sportseventsapp.domain.SportsEventsRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindFavoriteMarksStorage(favoriteMarksStorageImpl: FavoriteMarksStorageImpl): FavoriteMarksStorage

    @Binds
    fun bindSportsEventsRepository(sportsEventsRepositoryImpl: SportsEventsRepositoryImpl): SportsEventsRepository
}
