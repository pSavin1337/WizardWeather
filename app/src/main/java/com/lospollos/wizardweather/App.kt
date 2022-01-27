package com.lospollos.wizardweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.room.Room
import com.lospollos.wizardweather.data.database.WeatherDB

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        database = Room
            .databaseBuilder(this, WeatherDB::class.java, getString(R.string.db_name))
            .build()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var database: WeatherDB
            private set
    }

}