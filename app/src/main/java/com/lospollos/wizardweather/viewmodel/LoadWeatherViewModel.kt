package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lospollos.wizardweather.App.Companion.context
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.network.ImageLoader
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import kotlinx.coroutines.*

class LoadWeatherViewModel : ViewModel() {

    private val weatherItems = MutableLiveData<List<WeatherResponseModel>>()
    private val message = MutableLiveData<String>()
    private val isLoading = MutableLiveData(false)
    private val icon = MutableLiveData<List<Bitmap>>()

    fun getWeatherItems(): LiveData<List<WeatherResponseModel>> = weatherItems
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
                    mapper = WeatherResponseMapper(),
                    errorMapper = WeatherErrorMapper()
                ).execute(cityName = city)
            }
            val loadedIcon = withContext(Dispatchers.IO) {
                when (result) {
                    is Result.Success -> {
                        val imageLinks = ImageLoader.loadImage(result)
                        ImageLoader.loadImageFromStorage(imageLinks)
                    }
                    is Result.LoadedFromDB -> {
                        val imageLinks: ArrayList<String> = ArrayList(dayCount)
                        for (resultItem in result.items!!) {
                            imageLinks.add(resultItem.weatherIconUrl)
                        }
                        ImageLoader.loadImageFromStorage(imageLinks)
                    }
                    else -> null
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
                weatherItems.value = result.items!!
                icon.value = loadedIcon!!
            }
            is Result.Error -> handleError(result)
        }
    }

    private fun handleError(result: Result.Error) {
        when (result) {
            is Result.Error.NoNetwork -> message.value = context.getString(R.string.no_network)
            is Result.Error.NotFound -> message.value = result.error.message
            is Result.Error.Unknown -> message.value = context.getString(R.string.unknown_error)
        }
    }

    fun openShareMenu(shareText: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

}