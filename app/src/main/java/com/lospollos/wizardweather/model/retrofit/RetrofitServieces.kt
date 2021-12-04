package com.lospollos.wizardweather.model.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitServieces {

    val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val coroutinesWeatherApi = getRetrofit(BASE_URL).create(WeatherApi::class.java)

    private fun getRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getClient() = OkHttpClient.Builder()
        .connectTimeout(5L, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY// чтобы в логах отображалось
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("Content-Type", "application/json")
            builder.addHeader("Authorization", "Bearer my_token")
            chain.proceed(builder.build())
        })
        .build()
}