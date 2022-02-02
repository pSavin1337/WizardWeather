package com.lospollos.wizardweather.viewmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lospollos.wizardweather.App.Companion.context
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.ImageLoader
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    private val disposableContainer = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposableContainer.dispose()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    fun loadWeather(city: String) {
        Log.i("THREAD1", Thread.currentThread().name)
        var res: Result? = null
        Observable.create<Result> { obs ->
            WeatherInteractor(
                mapper = WeatherResponseMapper(),
                errorMapper = WeatherErrorMapper()
            )
                .execute(cityName = city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { isLoading.postValue(true) }
                .doAfterTerminate {
                    res?.let {res ->
                        Observable.create<ArrayList<Bitmap>> { obsIc ->
                            ImageLoader
                                .loadIcons(res)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    obsIc.onNext(it)
                                    obsIc.onComplete()
                                }
                        }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doAfterTerminate { obs.onComplete() }
                            .subscribe { handleResultIcons(res, it) }
                    }
                }
                .subscribe { result ->
                    Log.i("THREAD1", Thread.currentThread().name)
                    res = result
                    obs.onNext(result)
                }
                .let(disposableContainer::add)
        }
            .subscribeOn(Schedulers.io())
            .doAfterTerminate { isLoading.postValue(false) }
            .subscribe { handleResultWeather(it) }
            .let(disposableContainer::add)

    }

    private fun handleResultWeather(result: Result) {
        when (result) {
            is Result.Success -> {
                weatherItems.value = result.items
            }
            is Result.LoadedFromDB -> {
                weatherItems.value = result.items!!
            }
            is Result.Error -> handleError(result)
        }
    }

    private fun handleResultIcons(result: Result, loadedIcon: ArrayList<Bitmap>?) {
        when (result) {
            is Result.Success -> {
                icon.value = loadedIcon!!
            }
            is Result.LoadedFromDB -> {
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