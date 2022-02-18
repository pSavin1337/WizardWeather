package com.lospollos.wizardweather.dagger

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class ConnectivityManagerModule {

    @Provides
    fun manager(context: Context) = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}