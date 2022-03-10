package com.lospollos.wizardweather.view.widget

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import kotlinx.coroutines.*

object WidgetInfoLoader {

    var message: String? = null
    private var weather: List<WeatherResponseModel>? = null
    private val job = Job()
    private var scope = CoroutineScope(job + Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadWeather(city: String, callback: (weather: List<WeatherResponseModel>?) -> Unit) {
        scope.launch {
            async(Dispatchers.IO) {
                weather = handleResult(
                    WeatherInteractor(
                        mapper = WeatherResponseMapper(),
                        errorMapper = WeatherErrorMapper()
                    ).execute(cityName = city)
                )
            }.invokeOnCompletion {
                launch(Dispatchers.Main) {
                    callback(weather)
                }
            }
        }
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
            is Result.Error.NoNetwork -> App.context.getString(R.string.no_network)
            is Result.Error.NotFound -> result.error.message
            is Result.Error.Unknown -> App.context.getString(R.string.unknown_error)
        }
    }

}