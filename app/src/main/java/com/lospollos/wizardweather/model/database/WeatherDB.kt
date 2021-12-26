package com.lospollos.wizardweather.model.database

import androidx.room.RoomDatabase
import androidx.room.Database

@Database(
    entities = [EntityWeatherDB::class, CityEntity::class],
    version = 1
)
abstract class WeatherDB : RoomDatabase() {
    abstract val weatherDao: WeatherDao
}