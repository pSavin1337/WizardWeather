package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.data.WeatherInteractor
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {
    @Provides
    fun interactor(): WeatherInteractor = WeatherInteractor()
}