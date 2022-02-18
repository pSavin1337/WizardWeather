package com.lospollos.wizardweather.dagger

import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.network.ImageLoader
import com.lospollos.wizardweather.view.ViewPagerAdapter
import com.lospollos.wizardweather.view.services.NotificationInfoLoader
import com.lospollos.wizardweather.view.services.WeatherNotificationWorker
import com.lospollos.wizardweather.viewmodel.CityListViewModel
import com.lospollos.wizardweather.viewmodel.CityListViewModelFactory
import com.lospollos.wizardweather.viewmodel.LoadWeatherViewModel
import com.lospollos.wizardweather.viewmodel.LoadWeatherViewModelFactory
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Component(modules = [
    ContextModule::class,
    WeatherDBProviderModule::class,
    CityDBProviderModule::class,
    MapperModule::class,
    ConnectivityManagerModule::class,
    ImageLoaderModule::class,
    RetrofitModule::class,
    InteractorModule::class,
    WorkerModule::class,
    NotificationLoaderModule::class
])
interface AppComponent {

    fun inject(interactor: WeatherInteractor)
    fun inject(imageLoader: ImageLoader)
    fun inject(viewModel: CityListViewModelFactory)
    fun inject(viewModel: LoadWeatherViewModelFactory)
    fun inject(worker: WeatherNotificationWorker)
    fun inject(notificationInfoLoader: NotificationInfoLoader)
    fun inject(adapter: ViewPagerAdapter)

}