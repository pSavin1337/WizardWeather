package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.lospollos.wizardweather.App.Companion.appComponent
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.dagger.WorkerModule
import com.lospollos.wizardweather.data.database.CityDBProvider
import com.lospollos.wizardweather.view.City
import com.lospollos.wizardweather.view.services.WeatherNotificationWorker
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CityListViewModel(
    @SuppressLint("StaticFieldLeak")
    var context: Context,
    var cityDBProvider: CityDBProvider,
    var workerBuilder: PeriodicWorkRequest.Builder
) : ViewModel() {

    private val cityList = MutableLiveData<List<City>?>()
    fun getCityListLiveData(): LiveData<List<City>?> = cityList

    private val job = Job()
    private val vmScope = CoroutineScope(job + Dispatchers.Main.immediate)
    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCleared() {
        super.onCleared()
        vmScope.cancel()
    }

    fun getCityList() = vmScope.launch {
        cityList.value = withContext(Dispatchers.IO) {
            cityDBProvider.getCityList()
        }
    }

    fun openNotificationWorker(cityName: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(context.getString(R.string.worker_tag))

        val workRequest: WorkRequest
        val dataWeather = Data.Builder().putString("cityName", cityName).build()

        workRequest = workerBuilder.setInputData(dataWeather).build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun closeNotificationWorker() {
        notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(101)
        WorkManager.getInstance(context).cancelAllWorkByTag(context.getString(R.string.worker_tag))
    }

    fun updateCityList(cityList: List<City>) = vmScope.launch {
        withContext(Dispatchers.IO) {
            cityDBProvider.updateCityList(cityList)
        }
    }

}