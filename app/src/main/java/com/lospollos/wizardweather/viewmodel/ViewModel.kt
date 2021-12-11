package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem
import com.lospollos.wizardweather.model.network.ImageLoader
import com.lospollos.wizardweather.model.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.model.network.mappers.WeatherListItemMapper
import com.lospollos.wizardweather.model.network.retrofit.CoroutinesWeatherInteractor
import com.lospollos.wizardweather.model.network.retrofit.Result
import kotlinx.coroutines.*

class ViewModel: ViewModel() {

    val weatherItems = MutableLiveData<List<List<BaseItemAdapterItem>>>()
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)
    var icon = MutableLiveData<List<Bitmap>>()
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
                CoroutinesWeatherInteractor(
                    mapper = WeatherListItemMapper(),
                    errorMapper = WeatherErrorMapper()
                ).execute(cityName = city)
            }
            handleResult(result)
            isLoading.value = false
        }
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> {
                weatherItems.value = result.items
                icon.value = ImageLoader.loadImage(result)
            }
            is Result.LoadedFromDB -> {
                weatherItems.value = result.items.first
                icon.value = result.items.second
            }
            is Result.Error -> handleError(result)
        }
    }

    private fun handleError(result: Result.Error) {
        when (result) {
            Result.Error.NoNetwork -> message.value = "Нет сети"
            is Result.Error.NotFound -> message.value = result.error.message
            is Result.Error.Unknown -> message.value = "Неизветсная ошибка"
        }
    }

}