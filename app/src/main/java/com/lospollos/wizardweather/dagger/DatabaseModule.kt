package com.lospollos.wizardweather.dagger

import android.content.Context
import androidx.room.Room
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.database.WeatherDB
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    fun database(context: Context) = Room
        .databaseBuilder(context, WeatherDB::class.java, context.getString(R.string.db_name))
        .build()

}