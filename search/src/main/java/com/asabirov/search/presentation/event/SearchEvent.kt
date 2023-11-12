package com.asabirov.search.presentation.event

import com.asabirov.search.domain.model.search_by_text.LocationModel

sealed interface SearchEvent {

    data object OnSearch : SearchEvent
    data class OnChangeCityName(val cityName: String) : SearchEvent
    data class OnAddPlaceByEditTextField(val placeName: String) : SearchEvent
    data class OnAddPlaceByClickTag(val placeName: String) : SearchEvent
    data class OnRemovePlace(val placeName: String) : SearchEvent
    data object OnRemoveAllPlaces : SearchEvent
    data class OnClickShowResultsOnMap(val locations: List<LocationModel>) : SearchEvent
}