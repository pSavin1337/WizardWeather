package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
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
    var icon = MutableLiveData<ArrayList<RequestBuilder<Drawable>>>()

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
            handleResult(result, context)
            isLoading.value = false
        }
    }

    private fun handleResult(result: Result, context: Context) {
        when (result) {
            is Result.Success -> {
                weatherItems.value = result.items
                val iconWeatherList: ArrayList<RequestBuilder<Drawable>> = ArrayList(5)
                for(i in 0..4)
                    iconWeatherList
                        .add(Glide.with(context)
                        .load((result.items[i][5] as BaseItemAdapterItem.Weather).iconUrl))
                icon.value = iconWeatherList
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