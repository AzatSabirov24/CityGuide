package com.asabirov.search.presentation.state

data class SearchState(
    val city: String = "",
    val placesNames: List<String> = emptyList(),
    val queryForSearch: String = "",
    val isSearching: Boolean = false
)
