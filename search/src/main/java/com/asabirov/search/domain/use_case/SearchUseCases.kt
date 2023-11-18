package com.asabirov.search.domain.use_case

data class SearchUseCases(
    val searchPlacesPaginated: SearchPlacesPaginated,
    val searchMorePlacesForMap: SearchMorePlacesForMap,
    val placeDetails: PlaceDetails
)