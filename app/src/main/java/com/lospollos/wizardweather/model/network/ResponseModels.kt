package com.lospollos.wizardweather.model.network

sealed class BaseItemAdapterItem {
    data class Temperature(
        val temp: String
    ) : BaseItemAdapterItem()

    data class Pressure(
        val value: String
    ) : BaseItemAdapterItem()

    data class Humidity(
        val value: String
    ) : BaseItemAdapterItem()

    data class Wind(
        val speed: String,
        val degree: String,
    ) : BaseItemAdapterItem()

    data class Clouds(
        val value: String
    ) : BaseItemAdapterItem()

    data class Weather(
        val id: Int,
        val description: String,
        val iconUrl: String,
    ) : BaseItemAdapterItem()

    data class Date(
        val date: String
    ): BaseItemAdapterItem()

}

data class NotFoundError(val message: String)
