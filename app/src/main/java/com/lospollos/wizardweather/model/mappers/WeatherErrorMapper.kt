package com.lospollos.wizardweather.model.mappers

import com.lospollos.wizardweather.model.NotFoundError
import com.lospollos.wizardweather.model.WeatherErrorModel

class WeatherErrorMapper : EntityToModelMapper<WeatherErrorModel, NotFoundError> {
    override fun mapEntity(entity: WeatherErrorModel): NotFoundError {
        return NotFoundError(message = entity.message)
    }
}