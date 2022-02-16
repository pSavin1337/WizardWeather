package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.data.WeatherInteractor
import dagger.Component

@Component(modules = [
    WeatherDBProviderModule::class,
    RetrofitModule::class,
    ContextModule::class,
    CityDBProviderModule::class,
    InteractorModule::class
])
interface InteractorComponent {
    fun inject(interactor: WeatherInteractor)
}
