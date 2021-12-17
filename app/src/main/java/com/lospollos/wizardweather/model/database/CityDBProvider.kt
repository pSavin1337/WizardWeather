package com.lospollos.wizardweather.model.database

import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.view.City

object CityDBProvider {

    private val db: WeatherDB = App.database

    fun updateCityList(cityList: List<City>) {
        db.weatherDao.deleteCityList(db.weatherDao.getCityList())
        val cityEntityList: ArrayList<CityEntity> = ArrayList()
        cityList.forEach {
            cityEntityList.add(CityEntity(cityName = it.cityName, isFavorite = it.isFavorite))
        }
        db.weatherDao.insertCityList(cityEntityList)
    }

    fun getCityList(): List<City> {
        val list = db.weatherDao.getCityList()
        val result: ArrayList<City> = ArrayList()
        list.forEach {
            result.add(City(it.cityName, it.isFavorite))
        }
        return result
    }

}