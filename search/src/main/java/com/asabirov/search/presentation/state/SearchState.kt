package com.asabirov.search.presentation.state

import com.asabirov.search.domain.model.search_by_text.SearchByTextModel

data class SearchState(
    val query: String = "",
    val cityName: String = "",
    val searchResult: SearchByTextModel? = null,
    val isSearching: Boolean = false
)
