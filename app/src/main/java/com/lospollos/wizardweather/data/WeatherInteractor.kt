package com.lospollos.wizardweather.data

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.App.Companion.appComponent
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.dagger.ContextModule
import com.lospollos.wizardweather.data.database.WeatherDBProvider
import com.lospollos.wizardweather.data.network.*
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import com.lospollos.wizardweather.data.network.retrofit.RetrofitServices
import com.lospollos.wizardweather.data.network.retrofit.WeatherApi
import retrofit2.Response
import javax.inject.Inject

class WeatherInteractor {

    @Inject
    lateinit var weatherDBProvider: WeatherDBProvider

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var weatherApi: WeatherApi

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var mapper: WeatherResponseMapper

    @Inject
    lateinit var cm: ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun execute(cityName: String): Result {

        appComponent.inject(this)

        if (!isNetworkAvailable())
            return if (weatherDBProvider.getWeatherByCityName(cityName).isEmpty()) {
                Result.Error.NoNetwork
            } else {
                Result.LoadedFromDB(weatherDBProvider.getWeatherByCityName(cityName))
            }
        else {
            val weatherData: Result
            try {
                val response = weatherApi.loadWeatherByCityName(cityName)
                weatherData = handleResponse(response)

                weatherDBProvider.deleteOldWeatherByCityName(cityName)
                weatherDBProvider.insertWeatherForCity(
                    (weatherData as Result.Success).items,
                    imageLoader.loadImage(weatherData),
                    cityName
                )
            } catch (e: Exception) {
                return Result.Error.Unknown
            }
            return weatherData
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkAvailable(): Boolean {
        /*val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager*/
        return cm.activeNetwork != null
    }

    private fun handleResponse(
        response: Response<WeatherSuccessModel>
    ): Result {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body == null)
                try {
                    Result.Error.Unknown
                } catch (e: Exception) {
                    Result.Error.Unknown
                }
            else
                try {
                    Result.Success(mapper.mapEntity(body))
                } catch (e: Exception) {
                    Result.Error.Unknown
                }
        } else {
            Result.Error.Unknown
        }

    }
}

sealed class Result {
    data class Success(val items: List<WeatherResponseModel>) : Result()

    data class LoadedFromDB(
        val items: List<WeatherResponseModel>?
    ) : Result()

    sealed class Error : Result() {
        object NotFound : Error()
        object Unknown : Error()
        object NoNetwork : Error()
    }
}
