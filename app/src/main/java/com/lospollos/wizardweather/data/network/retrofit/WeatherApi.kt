package com.lospollos.wizardweather.data.network.retrofit

import com.lospollos.wizardweather.data.network.WeatherSuccessModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast?units=metric&appid=fca26ed2fe53765d1d6bdbf57311ec7a")
    suspend fun loadWeatherByCityName(
        @Query("q") cityName: String?
    ): Response<WeatherSuccessModel>
}