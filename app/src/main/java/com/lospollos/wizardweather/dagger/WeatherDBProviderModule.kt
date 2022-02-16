package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.data.database.WeatherDB
import com.lospollos.wizardweather.data.database.WeatherDBProvider
import dagger.Module
import dagger.Provides

@Module(includes = [DatabaseModule::class])
class WeatherDBProviderModule {

    @Provides
    fun weatherDBProvider(database: WeatherDB) = WeatherDBProvider(database)

}