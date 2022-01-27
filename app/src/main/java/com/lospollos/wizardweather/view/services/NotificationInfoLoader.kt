package com.lospollos.wizardweather.view.services

import android.os.Build
import androidx.annotation.RequiresApi
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper

object NotificationInfoLoader {

    var message: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun loadWeather(city: String): List<WeatherResponseModel>? = handleResult(
        WeatherInteractor(
            mapper = WeatherResponseMapper(),
            errorMapper = WeatherErrorMapper()
        ).execute(cityName = city)
    )

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
            is Result.Error.NoNetwork -> App.context.getString(R.string.no_network)
            is Result.Error.NotFound -> result.error.message
            is Result.Error.Unknown -> App.context.getString(R.string.unknown_error)
        }
    }

}