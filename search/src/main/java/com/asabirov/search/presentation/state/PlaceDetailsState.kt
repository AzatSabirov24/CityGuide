package com.asabirov.search.presentation.state

import com.asabirov.search.domain.model.place_details.PlaceDetailsModel

data class PlaceDetailsState(
    val place: PlaceDetailsModel? = null,
    val isSearching: Boolean = false,
    val error: String = ""
)
