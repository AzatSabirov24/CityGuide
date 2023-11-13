package com.asabirov.search.presentation.state

import com.asabirov.search.domain.model.places.PlaceModel

data class PlacesState(
    val places: List<PlaceModel> = emptyList()
)
