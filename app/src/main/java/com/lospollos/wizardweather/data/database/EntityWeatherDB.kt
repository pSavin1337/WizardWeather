package com.lospollos.wizardweather.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntityWeatherDB(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var city: String,
    var date: String,
    var temperature: String,
    var pressure: String,
    var humidity: String,
    var windSpeed: String,
    var windDegree: String,
    var clouds: String,
    var weatherId: Int,
    var weatherDescription: String,
    var icon: String
)

@Entity
class CityEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cityName: String,
    var isFavorite: Boolean
)
