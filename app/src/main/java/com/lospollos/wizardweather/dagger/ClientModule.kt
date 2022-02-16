package com.lospollos.wizardweather.dagger

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
class ClientModule {

    @Provides
    fun client(interceptor: Interceptor) = OkHttpClient.Builder()
        .connectTimeout(5L, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    @Provides
    fun interceptor() = Interceptor { chain ->
        val builder = chain.request().newBuilder()
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Authorization", "Bearer my_token")
        chain.proceed(builder.build())
    }

}