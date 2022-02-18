package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.network.ImageLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ImageLoaderModule {
    @Provides
    fun imageLoader(): ImageLoader = ImageLoader()
}