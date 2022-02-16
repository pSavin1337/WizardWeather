package com.lospollos.wizardweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.lospollos.wizardweather.dagger.AppComponent
import com.lospollos.wizardweather.dagger.ContextModule
import com.lospollos.wizardweather.dagger.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }

}