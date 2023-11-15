package com.asabirov.search.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asabirov.core.utils.event.UiEvent
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.use_case.SearchUseCases
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.place_details.state.PlaceDetailsState
import com.asabirov.search.presentation.search.state.PlacesState
import com.asabirov.search.presentation.search.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    var placeDetailsState by mutableStateOf(PlaceDetailsState(place = PlaceDetailsModel()))
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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

//            is SearchEvent.OnClickShowPlacesOnMap -> {
//                openSearchResultsOnMap(event.locations)
//            }

            is SearchEvent.OnSelectPlace -> {
                getPlaceDetails(event.id)
            }

//            is SearchEvent.OnClickShowPlaceDetailsOnMap -> {
//
//            }
        }
    }

    // Search places
    private fun executeSearch() {
        viewModelScope.launch {
            searchState = searchState.copy(
                isSearching = true
            )
            placesState = placesState.copy(places = emptyList())
            searchUseCases.searchPlaces(searchState.queryForSearch)
                .onSuccess { searchResult ->
                    println("qqq SearchViewModel->onSuccess->${searchResult.places}")
                    searchState = searchState.copy(
                        isSearching = false
                    )
                    placesState = placesState.copy(
                        places = searchResult.places
                    )
                }
                .onFailure {
                    searchState = searchState.copy(
                        isSearching = false
                    )
                    println("qqq SearchViewModel->onFailure->${it.message}")
                }
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

//    private fun openSearchResultsOnMap(
//        locations: List<LocationModel>
//    ) {
//        viewModelScope.launch {
//            _uiEvent.send(UiEvent.OpenScreen(data = locations))
//        }
//    }

    // Get place details

    private fun getPlaceDetails(id: String) {
        println("qqq SearchViewModel->getPlaceDetails->${id}")
        viewModelScope.launch {
            placeDetailsState = placeDetailsState.copy(
                isSearching = true
            )
            searchUseCases.placeDetails(id)
                .onSuccess { place ->
                    println("qqq getPlaceDetails->onSuccess->${place}")
                    placeDetailsState = placeDetailsState.copy(
                        isSearching = false,
                        place = place
                    )
                }
                .onFailure {
                    placeDetailsState = placeDetailsState.copy(
                        isSearching = false,
                        error = it.message ?: ""
                    )
                    println("qqq getPlaceDetails->onFailure->${it.message}")
                }
        }
    }
}