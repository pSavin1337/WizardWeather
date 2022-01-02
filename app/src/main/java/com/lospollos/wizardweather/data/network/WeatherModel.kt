package com.lospollos.wizardweather.data.network

import com.google.gson.annotations.SerializedName

data class WeatherSuccessModel(
    val city: City,
    val cod: String,
    val message: String,
    val cnt: Int,
    val list: List<WeatherModel>,
) {

    data class City(
        val id: Int,
        val name: String,
        val coord: Coord,
        val country: String,
        val population: Int,
        val timezone: Int
    ) {
        data class Coord(
            val lon: Double,
            val lat: Double
        )
    }

    data class WeatherModel(

        val dt: String,
        val main: Main,
        val weather: List<Weather>,
        val clouds: Clouds,
        val wind: Wind,
        val rain: Rain,
        val snow: Snow,
        val sys: Sys,
        val dt_txt: String,
        val visibility: String,
        val pop: String

    ) {
        data class Main(
            val temp: Double,
            val temp_min: Double,
            val temp_max: Double,
            val pressure: Int,
            val humidity: Int,
            val sea_level: Double,
            val grnd_level: Double,
            val temp_kf: Double,
            val feels_like: Double
        )

        data class Weather(
            val id: Int,
            val main: String,
            val description: String,
            val icon: String
        )

        data class Clouds(
            val all: Int,
        )

        data class Wind(
            val speed: Double,
            val deg: Int,
            val gust: Double,
        )

        data class Rain(
            @field:SerializedName("3h")
            val r: Double
        )

        data class Snow(
            @field:SerializedName("3h")
            val s: Double
        )

        data class Sys(
            val pod: String
        )

    }
}

data class WeatherErrorModel(
    val cod: String,
    val message: String
)