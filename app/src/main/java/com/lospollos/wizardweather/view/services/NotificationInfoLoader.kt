package com.lospollos.wizardweather.view.services

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.Constants
import com.lospollos.wizardweather.R
import com.lospollos.wizardweather.data.ImageLoader
import com.lospollos.wizardweather.data.network.mappers.WeatherErrorMapper
import com.lospollos.wizardweather.data.WeatherInteractor
import com.lospollos.wizardweather.data.Result
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.mappers.WeatherResponseMapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object NotificationInfoLoader {

    var message: String? = null

    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.M)
    fun loadWeather(city: String): Observable<List<WeatherResponseModel>> {
        return Observable.create<List<WeatherResponseModel>> {
            WeatherInteractor(
                mapper = WeatherResponseMapper(),
                errorMapper = WeatherErrorMapper()
            )
                .execute(cityName = city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    handleResult(result)?.let { it1 -> it.onNext(it1) }
                }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun handleResult(result: Result): List<WeatherResponseModel>? {
        message = null
        return when (result) {
            is Result.Success -> result.items
            is Result.LoadedFromDB -> result.items
            is Result.Error -> {
                handleError(result)
                null
            }
        }
    }

    private fun handleError(result: Result.Error) {
        message = when (result) {
            is Result.Error.NoNetwork -> App.context.getString(R.string.no_network)
            is Result.Error.NotFound -> result.error.message
            is Result.Error.Unknown -> App.context.getString(R.string.unknown_error)
        }
    }

}