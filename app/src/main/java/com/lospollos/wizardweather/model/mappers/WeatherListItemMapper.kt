package com.lospollos.wizardweather.model.mappers

import com.lospollos.wizardweather.model.BaseItemAdapterItem
import com.lospollos.wizardweather.model.WeatherSuccessModel

class WeatherListItemMapper :
        EntityToModelMapper<WeatherSuccessModel, List<List<BaseItemAdapterItem>>> {
    override fun mapEntity(entity: WeatherSuccessModel): List<List<BaseItemAdapterItem>> {

        /*val dayCount = entity.cnt

        val tempList: ArrayList<Double> = ArrayList(dayCount)
        val presList: ArrayList<Int> = ArrayList(dayCount)
        val humList: ArrayList<Int> = ArrayList(dayCount)
        val speedList: ArrayList<Double> = ArrayList(dayCount)
        val degreeList: ArrayList<Int> = ArrayList(dayCount)
        val cloudsList: ArrayList<Int> = ArrayList(dayCount)
        val idList: ArrayList<Int> = ArrayList(dayCount)
        val descList: ArrayList<String> = ArrayList(dayCount)
        val iconList: ArrayList<String> = ArrayList(dayCount)
        for(i in 0..6) {
            tempList.add(entity.list[i].temp.day)
            presList.add(entity.list[i].pressure)
            humList.add(entity.list[i].humidity)
            speedList.add(entity.list[i].speed)
            degreeList.add(entity.list[i].deg)
            cloudsList.add(entity.list[i].clouds)
            idList.add(entity.list[i].weather[0].id)
            descList.add(entity.list[i].weather[0].description)
            iconList.add(entity.list[i].weather[0].icon)
        }*/
        val dayCount = entity.cnt
        val result: ArrayList<ArrayList<BaseItemAdapterItem>> = ArrayList(dayCount)
        for (i in 0..6) {
            val list: ArrayList<BaseItemAdapterItem> = ArrayList(6)
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
            result.add(list)
        }
        return result
    }
}