package com.asabirov.search.presentation.search.state

data class SearchState(
    val city: String = "",
    val placesNames: List<String> = emptyList(),
    val queryForSearch: String = "",
    val isSearching: Boolean = false
)
