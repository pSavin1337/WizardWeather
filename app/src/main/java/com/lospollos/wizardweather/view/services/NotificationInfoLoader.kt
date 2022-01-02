package com.lospollos.wizardweather.view.services

import android.os.Build
import androidx.annotation.RequiresApi
import com.lospollos.wizardweather.data.network.BaseItemAdapterItem
import com.lospollos.wizardweather.data.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.data.network.mappers.WeatherListItemMapper
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.Result

object NotificationInfoLoader {

    var message: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun loadWeather(city: String): List<List<BaseItemAdapterItem>>? = handleResult(
        WeatherInteractor(
            mapper = WeatherListItemMapper(),
            errorMapper = WeatherErrorMapper()
        ).execute(cityName = city)
    )

    private fun handleResult(result: Result): List<List<BaseItemAdapterItem>>? {
        message = null
        return when (result) {
            is Result.Success -> result.items
            is Result.LoadedFromDB -> result.items.first!!
            is Result.Error -> handleError(result)
        }
    }

    private fun handleError(result: Result.Error): List<List<BaseItemAdapterItem>>? {
        message = when (result) {
            is Result.Error.NoNetwork -> "Нет сети"
            is Result.Error.NotFound -> result.error.message
            is Result.Error.Unknown -> "Неизветсная ошибка"
        }
        return null
    }

}