package com.asabirov.search.presentation.event

sealed interface SearchEvent {

    data object OnSearch : SearchEvent
}