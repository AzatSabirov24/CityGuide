package com.asabirov.search.presentation.state

import com.asabirov.search.domain.model.search_by_text.PlaceModel

data class PlacesState(
    val places: List<PlaceModel> = emptyList()
)
