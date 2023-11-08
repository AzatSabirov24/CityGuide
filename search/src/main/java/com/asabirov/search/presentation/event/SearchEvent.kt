package com.asabirov.search.presentation.event

sealed interface SearchEvent {

    data object OnSearch : SearchEvent
    data class OnAddCityName(val name: String) : SearchEvent
    data class OnAddQuery(val query: String) : SearchEvent
    data class OnRemoveQuery(val query: String) : SearchEvent
}