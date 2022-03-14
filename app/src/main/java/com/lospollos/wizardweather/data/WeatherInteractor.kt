package com.lospollos.wizardweather.data

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.database.WeatherDBProvider
import com.lospollos.wizardweather.data.network.*
import com.lospollos.wizardweather.data.network.mappers.EntityToModelMapper
import com.lospollos.wizardweather.data.network.retrofit.RetrofitServices
import retrofit2.Response

class WeatherInteractor(
    private val mapper: EntityToModelMapper<WeatherSuccessModel, List<WeatherResponseModel>>,
    private val errorMapper: EntityToModelMapper<WeatherErrorModel, NotFoundError>
) {
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun execute(cityName: String): Result {
        if (!isNetworkAvailable())
            return if (App.database.weatherDao.getWeatherByCityName(cityName).isEmpty()) {
                Result.Error.NoNetwork
            }
            else {
                Result.LoadedFromDB(WeatherDBProvider.getWeatherByCityName(cityName))
            }
        else {
            val weatherData: Result
            try {
                val response = RetrofitServices.weatherApi
                    .loadWeatherByCityName(cityName)
                weatherData = handleResponse(response, mapper, errorMapper)
                val imageLinks = ImageLoader.loadImage(weatherData as Result.Success)
                var i = 0
                weatherData.items.forEach { weather ->
                    weather.weatherIconUrl = imageLinks[i++]
                }
                WeatherDBProvider.deleteOldWeatherByCityName(cityName)
                WeatherDBProvider.insertWeatherForCity(
                    weatherData.items,
                    imageLinks,
                    cityName
                )
            } catch (e: Exception) {
                return Result.Error.Unknown(
                    error = e.localizedMessage ?: e.message
                    ?: App.context.getString(R.string.unknown_error)
                )
            }
            return weatherData
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkAvailable(): Boolean {
        val cm = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }

    private fun handleResponse(
        response: Response<WeatherSuccessModel>,
        mapper: EntityToModelMapper<WeatherSuccessModel, List<WeatherResponseModel>>,
        errorMapper: EntityToModelMapper<WeatherErrorModel, NotFoundError>
    ): Result {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body == null)
                try {
                    Result.Error.Unknown(App.context.getString(R.string.empty_body))
                } catch (e: Exception) {
                    Result.Error.Unknown(
                        error = e.localizedMessage ?: e.message
                        ?: App.context.getString(R.string.unknown_error)
                    )
                }
            else
                try {
                    Result.Success(mapper.mapEntity(body))
                } catch (e: Exception) {
                    Result.Error.Unknown(
                        error = e.localizedMessage ?: e.message
                        ?: App.context.getString(R.string.unknown_error)
                    )
                }
        } else
            handleResponseError(response, errorMapper)

    }

    private fun handleResponseError(
        response: Response<*>,
        errorMapper: EntityToModelMapper<WeatherErrorModel, NotFoundError>
    ): Result.Error {
        return when (response.code()) {
            404 -> Result.Error.NotFound(
                error = errorMapper.mapEntity(
                    Gson().fromJson(response.errorBody()!!.string(), WeatherErrorModel::class.java)
                )
            )
            else -> Result.Error.Unknown(
                error = response.errorBody()?.string() ?: App.context.getString(
                    R.string.unknown_error
                )
            )
        }
    }
}

sealed class Result {
    data class Success(val items: List<WeatherResponseModel>) : Result()

    data class LoadedFromDB(
        val items: List<WeatherResponseModel>?
    ) : Result()

    sealed class Error : Result() {
        data class NotFound(val error: NotFoundError) : Error()
        data class Unknown(val error: String) : Error()
        object NoNetwork : Error()
    }
}
