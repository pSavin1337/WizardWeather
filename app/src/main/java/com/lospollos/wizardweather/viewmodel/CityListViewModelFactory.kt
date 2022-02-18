package com.lospollos.wizardweather.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import com.lospollos.wizardweather.App.Companion.appComponent
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.database.CityDBProvider
import com.lospollos.wizardweather.data.database.WeatherDBProvider
import com.lospollos.wizardweather.data.network.ImageLoader
import javax.inject.Inject

class CityListViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var cityDBProvider: CityDBProvider

    @Inject
    lateinit var workerBuilder: PeriodicWorkRequest.Builder

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        appComponent.inject(this)
        return try {
            CityListViewModel(context, cityDBProvider, workerBuilder) as T
        } catch (e: Exception) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}