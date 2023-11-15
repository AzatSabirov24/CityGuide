package com.asabirov.search.domain.model.places

data class PlacesModel(
    val nextPageToken: String?,
    val places: List<PlaceModel>
)
