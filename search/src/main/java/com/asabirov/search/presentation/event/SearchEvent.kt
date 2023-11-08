package com.asabirov.search.presentation.event

sealed interface SearchEvent {

    data object OnSearch : SearchEvent
    data class OnChangeCityName(val cityName: String) : SearchEvent
    data class OnAddPlaceByEditTextField(val placeName: String) : SearchEvent
    data class OnAddPlaceByClickTag(val placeName: String) : SearchEvent
    data class OnRemovePlace(val placeName: String) : SearchEvent
}