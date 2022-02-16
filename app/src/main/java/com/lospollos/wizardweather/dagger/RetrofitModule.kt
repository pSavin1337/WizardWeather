package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.data.network.retrofit.RetrofitServices
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.Gson

import com.google.gson.GsonBuilder
import com.lospollos.wizardweather.data.network.retrofit.WeatherApi
import retrofit2.create
import javax.inject.Singleton

@Module(includes = [ClientModule::class])
class RetrofitModule {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @Provides
    fun weatherApi(retrofit: Retrofit) =
        retrofit.create(WeatherApi::class.java)

    @Provides
    fun retrofit(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

}