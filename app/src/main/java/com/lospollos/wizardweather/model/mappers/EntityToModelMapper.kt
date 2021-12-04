package com.lospollos.wizardweather.model.mappers

interface EntityToModelMapper<Entity, Model> {
    fun mapEntity(entity: Entity): Model
}