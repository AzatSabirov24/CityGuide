package com.asabirov.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asabirov.search.domain.use_case.SearchUseCases
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.state.PlacesState
import com.asabirov.search.presentation.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCases: SearchUseCases
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val _placesState = MutableStateFlow(PlacesState())
    val placesState = _placesState.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> {
                executeSearch()
            }

            is SearchEvent.OnChangeCityName -> {
                _searchState.update {
                    it.copy(city = event.cityName)
                }
                updateQueryForSearch()
            }

            is SearchEvent.OnAddPlaceByClickTag -> {
                addPlace(event.placeName)
                updateQueryForSearch()
            }

            is SearchEvent.OnAddPlaceByEditTextField -> {
                onChangePlace(event.placeName)
                updateQueryForSearch()
            }

            is SearchEvent.OnRemovePlace -> {
                removePlace(event.placeName)
                updateQueryForSearch()
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            _searchState.update {
                it.copy(isSearching = true)
            }
            searchUseCases.searchByText(searchState.value.queryForSearch)
                .onSuccess { searchResult ->
                    println("qqq SearchViewModel->onSuccess->${searchResult.results}")
                    _searchState.update {
                        it.copy(
                            isSearching = false,
                            searchResult = searchResult,
                        )
                    }
                }
                .onFailure { println("qqq SearchViewModel->onFailure->${it.message}") }
        }
    }

    private fun onChangePlace(place: String) {
        val places = _searchState.value.placesNames.toMutableList()
        places.clear()
        places += place
        _searchState.update {
            it.copy(placesNames = places)
        }
    }

    private fun addPlace(place: String) {
        val places = _searchState.value.placesNames.toMutableList()
        places += place
        _searchState.update {
            it.copy(placesNames = places)
        }
    }

    private fun removePlace(place: String) {
        val places = _searchState.value.placesNames.toMutableList()
        places.remove(places.find { it.contains(place) })
        _searchState.update {
            it.copy(placesNames = places)
        }
    }

    private fun updateQueryForSearch() {
        _searchState.update {
            it.copy(queryForSearch = it.placesNames.joinToString("+") + "+in+${it.city}")
        }
        println("qqq SearchViewModel->updateQueryForSearch->${_searchState.value.queryForSearch}")
    }
}