package com.asabirov.search.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.asabirov.search.presentation.event.SearchEvent

class SearchComponent(
    componentContext: ComponentContext,
    private val onNavigateToPlaceDetails: (String) -> Unit
) : ComponentContext by componentContext {

    private var _placeId = MutableValue("")
    val placeId: Value<String> = _placeId

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSelectPlace -> onNavigateToPlaceDetails(placeId.value)
            else -> {}
        }
    }
}