package com.lospollos.wizardweather.data.database

import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.data.network.WeatherResponseModel

class WeatherDBProvider(private val db: WeatherDB) {

    fun insertWeatherForCity(
        weather: List<WeatherResponseModel>,
        icons: ArrayList<String>,
        city: String
    ) {
        val weatherModelForDB = ArrayList<EntityWeatherDB>()
        var i = 0
        weather.forEach {
            weatherModelForDB.add(
                EntityWeatherDB(
                    city = city,
                    date = it.date,
                    temperature = it.temp,
                    pressure = it.pressure,
                    humidity = it.humidity,
                    windSpeed = it.windSpeed,
                    windDegree = it.windDegree,
                    clouds = it.clouds,
                    weatherId = it.weatherId,
                    weatherDescription = it.weatherDescription,
                    icon = icons[i]
                )
            )
            i++
        }
        db.weatherDao.insertWeatherForCity(weatherModelForDB)
    }

    fun deleteOldWeatherByCityName(city: String) {
        db.weatherDao.deleteOldWeatherOfCity(getWeatherByCityNameInEntity(city))
    }

    fun getWeatherByCityName(city: String): List<WeatherResponseModel> {
        val weatherInEntity = db.weatherDao.getWeatherByCityName(city)
        val weatherData: ArrayList<WeatherResponseModel> = ArrayList(dayCount)
        weatherInEntity.forEach {
            weatherData.add(
                WeatherResponseModel(
                    it.temperature,
                    it.pressure,
                    it.humidity,
                    it.windSpeed,
                    it.windDegree,
                    it.clouds,
                    it.weatherId,
                    it.weatherDescription,
                    it.icon,
                    it.date
                )
            )
        }
        return weatherData
    }

    private fun getWeatherByCityNameInEntity(city: String): List<EntityWeatherDB> {
        return db.weatherDao.getWeatherByCityName(city)
    }

}