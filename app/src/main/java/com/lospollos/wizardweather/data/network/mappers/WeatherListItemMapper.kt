package com.lospollos.wizardweather.data.network.mappers

import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.Constants.responseInDayCount
import com.lospollos.wizardweather.data.network.WeatherResponseModel
import com.lospollos.wizardweather.data.network.WeatherSuccessModel

class WeatherResponseMapper :
    EntityToModelMapper<WeatherSuccessModel, List<WeatherResponseModel>> {
    override fun mapEntity(entity: WeatherSuccessModel): List<WeatherResponseModel> {

        val result: ArrayList<WeatherResponseModel> = ArrayList(dayCount)
        var i = 0
        for (j in 0 until dayCount) {
            result.add(
                WeatherResponseModel(
                    "Температура: ${entity.list[i].main.temp}",
                    "Давление: ${entity.list[i].main.pressure}",
                    "Влажность: ${entity.list[i].main.humidity}",
                    "Скорость ветра: ${entity.list[i].wind.speed} метров в секунду",
                    "Направление ветра: ${entity.list[i].wind.deg}°",
                    "Облачность: ${entity.list[i].clouds.all}",
                    entity.list[i].weather[0].id,
                    entity.list[i].weather[0].description,
                    "https://openweathermap.org/img/wn/${entity.list[i].weather[0].icon}@2x.png",
                    entity.list[i].dt_txt.split(" ")[0]
                )
            )
            i += responseInDayCount
        }
        return result
    }
}