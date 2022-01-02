package com.lospollos.wizardweather.data.network.mappers

import com.lospollos.wizardweather.data.network.NotFoundError
import com.lospollos.wizardweather.data.network.WeatherErrorModel

class WeatherErrorMapper : EntityToModelMapper<WeatherErrorModel, NotFoundError> {
    override fun mapEntity(entity: WeatherErrorModel): NotFoundError {
        return NotFoundError(message = entity.message)
    }
}