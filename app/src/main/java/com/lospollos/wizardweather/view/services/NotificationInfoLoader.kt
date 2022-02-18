package com.lospollos.wizardweather.view.services

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.App.Companion.appComponent
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class NotificationInfoLoader {

    var message: String? = null
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var weatherInteractor: WeatherInteractor

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun loadWeather(city: String): List<WeatherResponseModel>? {
        appComponent.inject(this)
        return handleResult(
            weatherInteractor.execute(cityName = city)
        )
    }

    private fun handleResult(result: Result): List<WeatherResponseModel>? {
        message = null
        return when (result) {
            is Result.Success -> result.items
            is Result.LoadedFromDB -> result.items
            is Result.Error -> {
                handleError(result)
                null
            }
        }
    }

    private fun handleError(result: Result.Error) {
        message = when (result) {
            is Result.Error.NoNetwork -> context.getString(R.string.no_network)
            is Result.Error.NotFound -> context.getString(R.string.unknown_error)
            is Result.Error.Unknown -> context.getString(R.string.unknown_error)
        }
    }

}