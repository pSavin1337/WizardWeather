package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.App.Companion.appComponent
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.dagger.WorkerModule
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.network.ImageLoader
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import kotlinx.coroutines.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class LoadWeatherViewModel(
    var context: Context,
    var imageLoader: ImageLoader,
    var weatherInteractor: WeatherInteractor
) : ViewModel() {

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
                weatherInteractor.execute(cityName = city)
            }
            val loadedIcon = withContext(Dispatchers.IO) {
                when (result) {
                    is Result.Success -> {
                        val imageLinks = imageLoader.loadImage(result)
                        imageLoader.loadImageFromStorage(imageLinks)
                    }
                    is Result.LoadedFromDB -> {
                        val imageLinks: ArrayList<String> = ArrayList(dayCount)
                        for (resultItem in result.items!!) {
                            imageLinks.add(resultItem.weatherIconUrl)
                        }
                        imageLoader.loadImageFromStorage(imageLinks)
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
            is Result.Error.NotFound -> message.value = context.getString(R.string.unknown_error)
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