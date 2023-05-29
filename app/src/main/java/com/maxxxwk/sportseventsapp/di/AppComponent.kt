package com.maxxxwk.sportseventsapp.di

import android.content.Context
import com.maxxxwk.sportseventsapp.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DispatchersModule::class, NetworkModule::class, RepositoryModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }
}
