package com.asabirov.search.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.asabirov.core.utils.event.UiEvent
import com.asabirov.core.utils.event.UiText
import com.asabirov.search.R
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.use_case.SearchUseCases
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.place_details.state.PlaceDetailsState
import com.asabirov.search.presentation.search.state.PlacesState
import com.asabirov.search.presentation.search.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
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

    var searchPagingFlow: Flow<PagingData<PlaceModel>>? = null

    fun onEvent(event: SearchEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchEvent.OnSearch -> {
                    getPlacesPaginated()
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

                is SearchEvent.OnSelectPlace -> {
                    getPlaceDetails(event.id)
                }

                is SearchEvent.OnDownloadMorePlaces -> {
                    getMorePlacesOnMap()
                }

                is SearchEvent.OnAddPlaceToState -> {
                    addPlacesToPlaceState(event.places)
                }
            }
        }
    }

    fun showSnackBar() {
        viewModelScope.launch {
            _uiEvent.send(
                UiEvent.ShowSnackbar(message = UiText.StringResource(R.string.geolocation_permissions_required))
            )
            println("qqq SearchViewModel->showSnackBar->")
        }
    }

    // Search places

    private fun getPlacesPaginated() {
        viewModelScope.launch {
            searchState = searchState.copy(
                isSearching = true
            )
            println("qqq SearchViewModel->getPlacesPaginated->")
            searchPagingFlow = searchUseCases.searchPlacesPaginated.invoke(
                searchState.queryForSearch,
                placesState.nextPageToken
            )
                .flow
                .cachedIn(viewModelScope)
        }
    }

    private fun addPlacesToPlaceState(places: List<PlaceModel>) {
        searchState = searchState.copy(isSearching = false)
        placesState = placesState.copy(places = places)
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

    private fun getMorePlacesOnMap() {
        searchState = searchState.copy(
            isSearching = true
        )
        viewModelScope.launch {
            searchUseCases.searchMorePlacesForMap(
                query = searchState.queryForSearch,
                nextPageToken = placesState.nextPageToken
            )
                .onSuccess { searchResult ->
                    val places = placesState.places.toMutableList()
                    places.addAll(searchResult.places)
                    println("qqq SearchViewModel->searchResult new places->${searchResult.nextPageToken}")
                    searchState = searchState.copy(
                        isSearching = false
                    )
                    placesState = placesState.copy(
                        nextPageToken = searchResult.nextPageToken,
                        places = places
                    )
                }
                .onFailure {
                    searchState = searchState.copy(
                        isSearching = false
                    )
                    println("qqq SearchViewModel->onFailure new places->${it.message}")
                }
        }
    }

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