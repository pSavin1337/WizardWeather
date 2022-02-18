package com.lospollos.wizardweather.dagger

import androidx.work.PeriodicWorkRequest
import com.lospollos.wizardweather.view.services.NotificationInfoLoader
import com.lospollos.wizardweather.view.services.WeatherNotificationWorker
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit

@Module(includes = [ContextModule::class])
class WorkerModule() {

    @Provides
    fun workerBuilder() = PeriodicWorkRequest.Builder(
        WeatherNotificationWorker::class.java, 1, TimeUnit.HOURS
    )

}