package com.asabirov.search.presentation.event

sealed interface SearchEvent {

    data object OnSearch : SearchEvent
    data class OnChangeCityName(val cityName: String) : SearchEvent
    data class OnChangePlace(val placeName: String) : SearchEvent
//    data class OnAddQuery(val query: String) : SearchEvent
//    data class OnRemoveQuery(val query: String) : SearchEvent
}