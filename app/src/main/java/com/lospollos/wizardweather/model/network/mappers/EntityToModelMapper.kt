package com.lospollos.wizardweather.model.network.mappers

interface EntityToModelMapper<Entity, Model> {
    fun mapEntity(entity: Entity): Model
}