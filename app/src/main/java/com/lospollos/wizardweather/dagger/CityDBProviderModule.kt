package com.lospollos.wizardweather.dagger

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.database.CityDBProvider
import com.lospollos.wizardweather.data.database.WeatherDB
import com.lospollos.wizardweather.data.database.WeatherDBProvider
import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
class CityDBProviderModule {

    @Provides
    fun weatherDBProvider(database: WeatherDB) = CityDBProvider(database)

}