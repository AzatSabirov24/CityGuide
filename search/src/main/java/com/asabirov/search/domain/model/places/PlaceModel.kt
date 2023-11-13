package com.asabirov.search.domain.model.places

data class PlaceModel(
    val id: String,
    val name: String,
    val photoUrl: String,
    val location: LocationModel,
    val isOpenNow: Boolean?,
    val rating: Double?
)
