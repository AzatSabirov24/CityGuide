package com.asabirov.search.presentation.search.state

import com.asabirov.search.domain.model.places.PlaceModel

data class PlacesState(
    val places: List<PlaceModel> = emptyList()
)
