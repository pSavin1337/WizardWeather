package com.lospollos.wizardweather.data

import com.lospollos.wizardweather.data.network.WeatherResponseModel

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
