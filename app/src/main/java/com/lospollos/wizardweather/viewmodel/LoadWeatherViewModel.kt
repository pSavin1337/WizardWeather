package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.network.BaseItemAdapterItem
import com.lospollos.wizardweather.data.network.ImageLoader
import com.lospollos.wizardweather.data.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.data.network.mappers.WeatherListItemMapper
import kotlinx.coroutines.*

class LoadWeatherViewModel : ViewModel() {

    private val weatherItems = MutableLiveData<List<List<BaseItemAdapterItem>>>()
    private val message = MutableLiveData<String>()
    private val isLoading = MutableLiveData(false)
    private val icon = MutableLiveData<List<Bitmap>>()

    fun getWeatherItems(): LiveData<List<List<BaseItemAdapterItem>>> = weatherItems
    fun getMessage(): LiveData<String> = message
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getIcon(): LiveData<List<Bitmap>> = icon

    private val job = Job()
    private val vmScope = CoroutineScope(job + Dispatchers.Main.immediate)

    override fun onCleared() {
        super.onCleared()
        vmScope.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    fun loadWeather(city: String) {
        vmScope.launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                WeatherInteractor(
                    mapper = WeatherListItemMapper(),
                    errorMapper = WeatherErrorMapper()
                ).execute(cityName = city)
            }
            val loadedIcon = withContext(Dispatchers.IO) {
                try {
                    ImageLoader.loadImage(result as Result.Success)
                } catch (e: Exception) {
                    null
                }
            }
            handleResult(result, loadedIcon)
            isLoading.value = false
        }
    }

    private fun handleResult(result: Result, loadedIcon: ArrayList<Bitmap>?) {
        when (result) {
            is Result.Success -> {
                weatherItems.value = result.items
                icon.value = loadedIcon!!
            }
            is Result.LoadedFromDB -> {
                weatherItems.value = result.items.first!!
                icon.value = result.items.second!!
            }
            is Result.Error -> handleError(result)
        }
    }

    private fun handleError(result: Result.Error) {
        when (result) {
            is Result.Error.NoNetwork -> message.value = "Нет сети"
            is Result.Error.NotFound -> message.value = result.error.message
            is Result.Error.Unknown -> message.value = "Неизветсная ошибка"
        }
    }

}