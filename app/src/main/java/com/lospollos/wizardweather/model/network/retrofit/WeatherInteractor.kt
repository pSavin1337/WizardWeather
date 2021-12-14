package com.lospollos.wizardweather.model.network.retrofit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.RequestBuilder
import com.google.gson.Gson
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.model.database.WeatherDBProvider
import com.lospollos.wizardweather.model.network.*
import com.lospollos.wizardweather.model.network.mappers.EntityToModelMapper
import retrofit2.Response

class CoroutinesWeatherInteractor(
    private val mapper: EntityToModelMapper<WeatherSuccessModel, List<List<BaseItemAdapterItem>>>,
    private val errorMapper: EntityToModelMapper<WeatherErrorModel, NotFoundError>
) {
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun execute(cityName: String): Result {
        if (!isNetworkAvailable())
            return if (App.database.weatherDao.getWeatherByCityName(cityName).isEmpty())
                Result.Error.NoNetwork
            else {
                Result.LoadedFromDB(WeatherDBProvider.getWeatherByCityName(cityName))
            }
        else {
            val weatherData: Result
            try {
                val response = RetrofitServieces
                    .coroutinesWeatherApi
                    .loadWeatherByCityName(cityName)
                weatherData = handleResponse(response, mapper, errorMapper)

                WeatherDBProvider.deleteOldWeatherByCityName(cityName)
                WeatherDBProvider.insertWeatherForCity(
                    (weatherData as Result.Success).items,
                    ImageLoader.loadImage(weatherData),
                    cityName)
            } catch (e: Exception) {
                return handleError(e)
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
        mapper: EntityToModelMapper<WeatherSuccessModel, List<List<BaseItemAdapterItem>>>,
        errorMapper: EntityToModelMapper<WeatherErrorModel, NotFoundError>
    ): Result {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body == null)
                try {
                    Result.Error.Unknown("Empty body")
                } catch (e: Exception) {
                    handleError(e)
                }
            else
                try {
                    Result.Success(mapper.mapEntity(body))
                } catch (e: Exception) {
                    handleError(e)
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
                error = response.errorBody()?.string() ?: "Unknown Error"
            )
        }
    }

    private fun handleError(e: Throwable): Result.Error =
        Result.Error.Unknown(error = e.localizedMessage ?: e.message ?: "Unknown Error")

}

sealed class Result {
    data class Success(val items: List<List<BaseItemAdapterItem>>) : Result()

    data class LoadedFromDB(val items: Pair<List<List<BaseItemAdapterItem>>?,
            List<Bitmap>?>) : Result()

    sealed class Error : Result() {
        data class NotFound(val error: NotFoundError) : Result.Error()
        data class Unknown(val error: String) : Result.Error()
        object NoNetwork : Result.Error()
    }
}
