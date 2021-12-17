package com.lospollos.wizardweather.model.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.lospollos.wizardweather.App
import com.lospollos.wizardweather.Constants
import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.model.network.BaseItemAdapterItem
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream


object WeatherDBProvider {
    private val db: WeatherDB = App.database

    fun insertWeatherForCity(weather: List<List<BaseItemAdapterItem>>,
                             icons: ArrayList<Bitmap>,
                             city: String) {
        val weatherModelForDB = ArrayList<EntityWeatherDB>()
        var i = 0
        val iconsLongArray: ArrayList<ByteArray> = ArrayList()
        icons.forEach {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            iconsLongArray.add(stream.toByteArray())
        }
        weather.forEach {
            weatherModelForDB.add(
                EntityWeatherDB(
                    city = city,
                    date = (it[Constants.DATE] as BaseItemAdapterItem.Date).date,
                    temperature = (it[Constants.TEMP] as BaseItemAdapterItem.Temperature).temp,
                    pressure = (it[Constants.PRES] as BaseItemAdapterItem.Pressure).value,
                    humidity = (it[Constants.HUMID] as BaseItemAdapterItem.Humidity).value,
                    windSpeed = (it[Constants.WIND] as BaseItemAdapterItem.Wind).speed,
                    windDegree = (it[Constants.WIND] as BaseItemAdapterItem.Wind).degree,
                    clouds = (it[Constants.CLOUDS] as BaseItemAdapterItem.Clouds).value,
                    weatherId = (it[Constants.WEATHER] as BaseItemAdapterItem.Weather).id,
                    weatherDescription = (it[Constants.WEATHER] as BaseItemAdapterItem.Weather)
                        .description,
                    icon = iconsLongArray[i]
                )
            )
            i++
        }
        db.weatherDao.insertWeatherForCity(weatherModelForDB)
    }

    fun deleteOldWeatherByCityName(city: String) {
        db.weatherDao.deleteOldWeatherOfCity(getWeatherByCityNameInEntity(city))
    }

    fun getWeatherByCityName(city: String): Pair<List<List<BaseItemAdapterItem>>,
            List<Bitmap>> {
        val weatherInEntity = db.weatherDao.getWeatherByCityName(city)
        val weatherData: ArrayList<ArrayList<BaseItemAdapterItem>> = ArrayList(dayCount)
        val icons: ArrayList<Bitmap> = ArrayList()
        weatherInEntity.forEach {
            val list: ArrayList<BaseItemAdapterItem> = ArrayList(7)
            list.add(BaseItemAdapterItem.Temperature(temp = it.temperature))
            list.add(BaseItemAdapterItem.Pressure(value = it.pressure))
            list.add(BaseItemAdapterItem.Humidity(value = it.humidity))
            list.add(BaseItemAdapterItem.Wind(speed = it.windSpeed, degree = it.windDegree))
            list.add(BaseItemAdapterItem.Clouds(value = it.clouds))
            list.add(BaseItemAdapterItem.Weather(id = it.weatherId,
                description = it.weatherDescription,
                iconUrl = ""))
            list.add(BaseItemAdapterItem.Date(date = it.date))
            weatherData.add(list)
            val inputStream: InputStream = ByteArrayInputStream(it.icon)
            val options = BitmapFactory.Options()
            BitmapFactory.decodeStream(inputStream, null, options)
                ?.let { it1 -> icons.add(it1) }
        }
        return Pair(weatherData, icons)
    }

    private fun getWeatherByCityNameInEntity(city: String): List<EntityWeatherDB> {
        return db.weatherDao.getWeatherByCityName(city)
    }

}