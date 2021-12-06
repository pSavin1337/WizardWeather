package com.lospollos.wizardweather.model.network.mappers

import com.lospollos.wizardweather.model.network.NotFoundError
import com.lospollos.wizardweather.model.network.WeatherErrorModel

class WeatherErrorMapper : EntityToModelMapper<WeatherErrorModel, NotFoundError> {
    override fun mapEntity(entity: WeatherErrorModel): NotFoundError {
        return NotFoundError(message = entity.message)
    }
}