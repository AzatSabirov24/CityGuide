package com.asabirov.search.domain.model.place_details

data class PlaceDetailsModel(
    val id: String? = "",
    val name: String? = "",
    val openingTime: List<String>? = emptyList(),
    val address: String? = "",
    val phoneNumber: String? = "",
    val location: PlaceDetailsLocationModel? = null,
    val photos: List<PhotoModel>? = emptyList(),
    val rating: Double? = 0.0
)
