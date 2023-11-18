package com.asabirov.search.presentation.event

import com.asabirov.search.domain.model.places.PlaceModel

sealed interface SearchEvent {

    data object OnSearch : SearchEvent
    data class OnChangeCityName(val cityName: String) : SearchEvent
    data class OnAddPlaceByEditTextField(val placeName: String) : SearchEvent
    data class OnAddPlaceByClickTag(val placeName: String) : SearchEvent
    data class OnRemovePlace(val placeName: String) : SearchEvent
    data object OnRemoveAllPlaces : SearchEvent
    data object OnDownloadMorePlaces : SearchEvent
    data class OnSelectPlace(val id: String) : SearchEvent
    data class OnAddPlaceToState(val places: List<PlaceModel>) : SearchEvent
    data class OnSelectPlaceOnMap(val placeModel: PlaceModel) : SearchEvent
}