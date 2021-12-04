package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lospollos.wizardweather.model.BaseItemAdapterItem
import com.lospollos.wizardweather.model.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.model.mappers.WeatherListItemMapper
import com.lospollos.wizardweather.model.retrofit.CoroutinesWeatherInteractor
import kotlinx.coroutines.*
import com.lospollos.wizardweather.model.retrofit.Result

class ViewModel: ViewModel() {

    val weatherItems = MutableLiveData<List<List<BaseItemAdapterItem>>>()
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)

    private val job = Job()
    private val vmScope = CoroutineScope(job + Dispatchers.Main.immediate)

    override fun onCleared() {
        super.onCleared()
        vmScope.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    fun loadWeather(city: String?, context: Context) {
        vmScope.launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                CoroutinesWeatherInteractor(
                    mapper = WeatherListItemMapper(),
                    errorMapper = WeatherErrorMapper(),
                    context
                ).execute(cityName = city)
            }
            isLoading.value = false
            handleResult(result)
        }
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> weatherItems.value = result.items
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