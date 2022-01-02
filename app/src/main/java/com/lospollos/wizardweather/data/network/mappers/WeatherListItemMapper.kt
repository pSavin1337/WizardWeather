package com.lospollos.wizardweather.data.network.mappers

import com.lospollos.wizardweather.Constants.dayCount
import com.lospollos.wizardweather.data.network.BaseItemAdapterItem
import com.lospollos.wizardweather.data.network.WeatherSuccessModel

class WeatherListItemMapper :
    EntityToModelMapper<WeatherSuccessModel, List<List<BaseItemAdapterItem>>> {
    override fun mapEntity(entity: WeatherSuccessModel): List<List<BaseItemAdapterItem>> {

        val result: ArrayList<ArrayList<BaseItemAdapterItem>> = ArrayList(dayCount)
        var i = 0
        for (j in 0..4) {
            val list: ArrayList<BaseItemAdapterItem> = ArrayList(7)
            list.add(
                BaseItemAdapterItem.Temperature(
                    temp = "Температура: ${entity.list[i].main.temp}"
                )
            )
            list.add(
                BaseItemAdapterItem.Pressure(
                    value = "Давление: ${entity.list[i].main.pressure}"
                )
            )
            list.add(
                BaseItemAdapterItem.Humidity(
                    value = "Влажность: ${entity.list[i].main.humidity}"
                )
            )
            list.add(
                BaseItemAdapterItem.Wind(
                    speed = "Скорость ветра: ${entity.list[i].wind.speed} метров в секунду",
                    degree = "Направление ветра: ${entity.list[i].wind.deg}°"
                )
            )
            list.add(
                BaseItemAdapterItem.Clouds(
                    value = "Облачность: ${entity.list[i].clouds.all}"
                )
            )
            list.add(
                BaseItemAdapterItem.Weather(
                    id = entity.list[i].weather[0].id,
                    description = entity.list[i].weather[0].description,
                    iconUrl =
                    "https://openweathermap.org/img/wn/${entity.list[i].weather[0].icon}@2x.png"
                )
            )
            list.add(
                BaseItemAdapterItem.Date(
                    date = entity.list[i].dt_txt.split(" ")[0]
                )
            )
            result.add(list)
            i += 8
        }
        return result
    }
}