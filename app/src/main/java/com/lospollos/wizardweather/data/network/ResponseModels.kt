package com.lospollos.wizardweather.data.network

data class WeatherResponseModel(
    val temp: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windDegree: String,
    val clouds: String,
    val weatherId: Int,
    val weatherDescription: String,
    var weatherIconUrl: String,
    val date: String
)

data class NotFoundError(val message: String)
