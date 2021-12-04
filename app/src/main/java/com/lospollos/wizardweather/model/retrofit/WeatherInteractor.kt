package com.lospollos.wizardweather.model.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.lospollos.wizardweather.model.BaseItemAdapterItem
import com.lospollos.wizardweather.model.NotFoundError
import com.lospollos.wizardweather.model.WeatherErrorModel
import com.lospollos.wizardweather.model.WeatherSuccessModel
import com.lospollos.wizardweather.model.mappers.EntityToModelMapper
import retrofit2.Response

class CoroutinesWeatherInteractor(
    private val mapper: EntityToModelMapper<WeatherSuccessModel, List<List<BaseItemAdapterItem>>>,
    private val errorMapper: EntityToModelMapper<WeatherErrorModel, NotFoundError>,
    private val context: Context
) {
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun execute(cityName: String?): Result {
        if (!isNetworkAvailable())
            return Result.Error.NoNetwork

        val response = try {
            RetrofitServieces
                .coroutinesWeatherApi
                .loadWeatherByCityName(cityName)
        } catch (e: Exception) {
            return handleError(e)
        }
        return handleResponse(response, mapper, errorMapper)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }

}

sealed class Result {

    data class Success(val items: List<List<BaseItemAdapterItem>>) : Result()

    sealed class Error : Result() {
        data class NotFound(val error: NotFoundError) : Result.Error()
        data class Unknown(val error: String) : Result.Error()
        object NoNetwork : Result.Error()
    }
}

private fun handleResponse(
    response: Response<WeatherSuccessModel>,
    mapper: EntityToModelMapper<WeatherSuccessModel, List<List<BaseItemAdapterItem>>>,
    errorMapper: EntityToModelMapper<WeatherErrorModel, NotFoundError>
): Result {
    return if (response.isSuccessful) {
        val body = response.body()
        if (body == null) {
            try {
                Result.Error.Unknown("Empty body")
            } catch (e: Exception) {
                handleError(e)
            }
        } else {
            try {
                Result.Success(mapper.mapEntity(body))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    } else {
        handleResponseError(response, errorMapper)
    }
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

private fun handleError(e: Throwable): Result.Error {
    return when (e) {
        //todo
        else -> Result.Error.Unknown(
            error = e.localizedMessage ?: e.message ?: "Unknown Error"
        )
    }
}


