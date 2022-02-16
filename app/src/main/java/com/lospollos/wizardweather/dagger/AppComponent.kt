package com.lospollos.wizardweather.dagger

import android.content.Context
import com.lospollos.wizardweather.data.database.CityDBProvider
import com.lospollos.wizardweather.data.database.WeatherDBProvider
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ContextModule::class,
    WeatherDBProviderModule::class,
    CityDBProviderModule::class
])
interface AppComponent {
    fun getContext(): Context

    @Singleton
    fun getWeatherDatabaseProvider(): WeatherDBProvider

    @Singleton
    fun getCityDatabaseProvider(): CityDBProvider

}