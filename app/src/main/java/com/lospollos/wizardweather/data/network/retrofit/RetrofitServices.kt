package com.lospollos.wizardweather.data.network.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitServices {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val weatherApi: WeatherApi = getRetrofit().create(WeatherApi::class.java)

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getClient() = OkHttpClient.Builder()
        .connectTimeout(5L, TimeUnit.SECONDS)
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("Content-Type", "application/json")
            builder.addHeader("Authorization", "Bearer my_token")
            chain.proceed(builder.build())
        })
        .build()
}