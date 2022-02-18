package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import dagger.Module
import dagger.Provides

@Module
class MapperModule {

    @Provides
    fun mapper() = WeatherResponseMapper()

}