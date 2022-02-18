package com.lospollos.wizardweather.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.database.CityDBProvider
import com.lospollos.wizardweather.data.database.WeatherDBProvider
import com.lospollos.wizardweather.data.network.ImageLoader
import javax.inject.Inject

class LoadWeatherViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var weatherInteractor: WeatherInteractor

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        App.appComponent.inject(this)
        return try {
            LoadWeatherViewModel(context, imageLoader, weatherInteractor) as T
        } catch (e: Exception) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}