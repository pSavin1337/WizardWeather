package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.view.services.NotificationInfoLoader
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class, InteractorModule::class])
class NotificationLoaderModule {

    @Provides
    fun notificationLoader() = NotificationInfoLoader()

}