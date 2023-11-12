package com.asabirov.search.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asabirov.search.domain.use_case.SearchUseCases
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.state.PlacesState
import com.asabirov.search.presentation.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCases: SearchUseCases
) : ViewModel() {

    var searchState by mutableStateOf(SearchState())
        private set

    var placesState by mutableStateOf(PlacesState())
        private set

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> {
                executeSearch()
            }

            is SearchEvent.OnChangeCityName -> {
                searchState = searchState.copy(city = event.cityName)
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

            is SearchEvent.OnRemoveAllPlaces -> {
                removeAllPlaces()
                updateQueryForSearch()
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            searchState = searchState.copy(
                isSearching = true
            )
            placesState = placesState.copy(places = emptyList())
            searchUseCases.searchByText(searchState.queryForSearch)
                .onSuccess { searchResult ->
                    println("qqq SearchViewModel->onSuccess->${searchResult.results}")
                    searchState = searchState.copy(
                        isSearching = false
                    )
                    placesState = placesState.copy(
                        places = searchResult.results
                    )
                    println("qqq SearchViewModel->executeSearch->${searchResult.results}")
                }
                .onFailure {
                    searchState = searchState.copy(
                        isSearching = false
                    )
                    println("qqq SearchViewModel->onFailure->${it.message}") }
        }
    }

    private fun onChangePlace(place: String) {
        val places = searchState.placesNames.toMutableList()
        places.clear()
        places += place
        searchState = searchState.copy(placesNames = places)
    }

    private fun addPlace(place: String) {
        val places = searchState.placesNames.toMutableList()
        places += place
        searchState = searchState.copy(placesNames = places)
    }

    private fun removePlace(place: String) {
        val places = searchState.placesNames.toMutableList()
        places.remove(places.find { it.contains(place) })
        searchState = searchState.copy(placesNames = places)
    }

    private fun removeAllPlaces() {
        val places = searchState.placesNames.toMutableList()
        places.clear()
        searchState = searchState.copy(placesNames = places)
    }

    private fun updateQueryForSearch() {
        searchState =
            searchState.copy(queryForSearch = searchState.placesNames.joinToString("+") + "+in+${searchState.city}")
        println("qqq SearchViewModel->updateQueryForSearch->${searchState.queryForSearch}")
    }
}