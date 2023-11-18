package com.asabirov.search.presentation.search.state

import com.asabirov.search.domain.model.places.PlaceModel

data class PlacesState(
    val nextPageToken: String? = null,
    val places: List<PlaceModel> = emptyList(),
    val selectedPlaceModel: PlaceModel? = null
)
