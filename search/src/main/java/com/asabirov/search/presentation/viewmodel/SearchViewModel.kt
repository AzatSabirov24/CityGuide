package com.asabirov.search.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.asabirov.core.utils.event.UiEvent
import com.asabirov.core.utils.event.UiText
import com.asabirov.search.R
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.LocationModel
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.use_case.SearchUseCases
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.place_details.state.PlaceDetailsState
import com.asabirov.search.presentation.search.state.PlacesState
import com.asabirov.search.presentation.search.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _searchResultPagingState =
        MutableSharedFlow<PagingData<PlaceModel>?>(
            replay = 1,
            extraBufferCapacity = 100,
            onBufferOverflow = BufferOverflow.DROP_LATEST,
        )
    val searchResultPagingState = _searchResultPagingState.asSharedFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun placesPagingFlow() = searchUseCases.searchPlaces.invoke(
        searchState.queryForSearch,
        placesState.nextPageToken
    ).flow.cachedIn(viewModelScope)

    fun onEvent(event: SearchEvent) {
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

    private fun getPlacesPaginated() {
        viewModelScope.launch {
            searchState = searchState.copy(
                isSearching = true
            )
            placesState = placesState.copy(places = emptyList())

//            try {
//                searchUseCases.searchPlaces.invoke(
//                    query = searchState.queryForSearch,
//                    nextPageToken = null
//                ).cachedIn(viewModelScope).collectLatest { pagingResult ->
//                    searchState = searchState.copy(
//                        isSearching = false
//                    )
//                    val name = pagingResult.map { it }
//                    println("qqq SearchViewModel->getPlacesPaginated->${name}")
//                    _searchResultPagingState.emit(pagingResult)
//                    createHotelItemsFromPageData(pagingResult)
//                }
//            } catch (_: Exception) {
//                /*nothing to do*/
//            }
        }
    }

    private suspend fun createHotelItemsFromPageData(
        pagingData: PagingData<PlaceModel>
    ): PlaceModel? {
        var place: PlaceModel? = null
        withContext(Dispatchers.Default) {
            pagingData.map { incomeSearchResult ->
                place = place?.copy(
                    id = incomeSearchResult.id,
                    name = incomeSearchResult.name,
                    photoUrl = "",
                    location = LocationModel(lat = 0.0, lng = 0.0),
                    isOpenNow = null,
                    rating = null
                )
            }
        }
        println("qqq SearchViewModel->createHotelItemsFromPageData->${place}")
        return place
    }

    // Search places
//    private fun executeSearch() {
//        viewModelScope.launch {
//            searchState = searchState.copy(
//                isSearching = true
//            )
//            placesState = placesState.copy(places = emptyList())
//            searchUseCases.searchPlaces(
//                query = searchState.queryForSearch,
//                nextPageToken = null
//            )
//                .onSuccess { searchResult ->
//                    println("qqq SearchViewModel->onSuccess->${searchResult.places}")
//                    searchState = searchState.copy(
//                        isSearching = false
//                    )
//                    placesState = placesState.copy(
//                        nextPageToken = searchResult.nextPageToken,
//                        places = searchResult.places
//                    )
//                }
//                .onFailure {
//                    searchState = searchState.copy(
//                        isSearching = false
//                    )
//                    println("qqq SearchViewModel->onFailure->${it.message}")
//                }
//        }
//    }

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
            searchUseCases.searchPlaces(
                query = searchState.queryForSearch,
                nextPageToken = placesState.nextPageToken
            )
//                .onSuccess { searchResult ->
//                    val places = placesState.places.toMutableList()
//                    places.addAll(searchResult.places)
//                    println("qqq SearchViewModel->searchResult new places->${searchResult.nextPageToken}")
//                    searchState = searchState.copy(
//                        isSearching = false
//                    )
//                    placesState = placesState.copy(
//                        nextPageToken = searchResult.nextPageToken,
//                        places = places
//                    )
//                }
//                .onFailure {
//                    searchState = searchState.copy(
//                        isSearching = false
//                    )
//                    println("qqq SearchViewModel->onFailure new places->${it.message}")
//                }
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