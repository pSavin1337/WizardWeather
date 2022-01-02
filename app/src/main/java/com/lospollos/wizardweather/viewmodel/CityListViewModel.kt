package com.lospollos.wizardweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lospollos.wizardweather.data.database.CityDBProvider
import com.lospollos.wizardweather.view.City
import kotlinx.coroutines.*

class CityListViewModel : ViewModel() {

    private val cityList = MutableLiveData<List<City>?>()
    fun getCityListLiveData(): LiveData<List<City>?> = cityList

    private val job = Job()
    private val vmScope = CoroutineScope(job + Dispatchers.Main.immediate)

    override fun onCleared() {
        super.onCleared()
        vmScope.cancel()
    }

    fun getCityList() = vmScope.launch {
        cityList.value = withContext(Dispatchers.IO) {
            CityDBProvider.getCityList()
        }
    }

    fun updateCityList(cityList: List<City>) = vmScope.launch {
        withContext(Dispatchers.IO) {
            CityDBProvider.updateCityList(cityList)
        }
    }

}