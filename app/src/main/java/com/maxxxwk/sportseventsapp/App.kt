package com.maxxxwk.sportseventsapp

import android.app.Application
import com.maxxxwk.sportseventsapp.di.AppComponent
import com.maxxxwk.sportseventsapp.di.DaggerAppComponent

class App : Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}
