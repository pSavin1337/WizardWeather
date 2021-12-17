package com.lospollos.wizardweather.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert
    fun insertWeatherForCity(weather: List<EntityWeatherDB>)

    @Delete
    fun deleteOldWeatherOfCity(weather: List<EntityWeatherDB>)

    @Query("SELECT * FROM entityweatherdb WHERE city LIKE :cityName")
    fun getWeatherByCityName(cityName: String) : List<EntityWeatherDB>


    @Insert
    fun insertCityList(cityList: List<CityEntity>)

    @Delete
    fun deleteCityList(cityList: List<CityEntity>)

    @Query("SELECT * FROM cityentity")
    fun getCityList(): List<CityEntity>

}