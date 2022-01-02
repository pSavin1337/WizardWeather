package com.lospollos.wizardweather.data.network.mappers

interface EntityToModelMapper<Entity, Model> {
    fun mapEntity(entity: Entity): Model
}