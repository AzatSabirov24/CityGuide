package com.asabirov.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.asabirov.search.domain.use_case.SearchUseCases
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCases: SearchUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> {
//                executeSearch()
            }

            is SearchEvent.OnChangeCityName -> {
                _state.update {
                    it.copy(city = event.cityName)
                }
                updateQueryForSearch()
                println("qqq SearchViewModel->onEvent->${_state.value.city}")
            }

            is SearchEvent.OnAddPlace -> {
                addPlace(event.placeName)
                updateQueryForSearch()
            }

            is SearchEvent.OnRemovePlace -> {
                removePlace(event.placeName)
                updateQueryForSearch()
            }
        }
    }

//    private fun executeSearch() {
//        viewModelScope.launch {
//            state = state.copy(
//                isSearching = true
//            )
//            searchUseCases.searchByText(state.query)
//                .onSuccess { searchResult ->
//                    println("qqq SearchViewModel->onSuccess->${searchResult.results}")
//                    state = state.copy(
//                        isSearching = false,
//                        searchResult = searchResult,
//                    )
//                }
//                .onFailure { println("qqq SearchViewModel->onFailure->${it.message}") }
//        }
//    }

    private fun addPlace(place: String) {
        val places = _state.value.places.toMutableList()
        places += place
        _state.update {
            it.copy(places = places)
        }
        println("qqq SearchViewModel->addPlace->${state.value.places}")
    }

    private fun removePlace(place: String) {
        val places = _state.value.places.toMutableList()
        places.remove(places.find { it.contains(place) })
        _state.update {
            it.copy(places = places)
        }
        println("qqq SearchViewModel->removePlace->${state.value.places}")
    }

    private fun updateQueryForSearch() {
        _state.update {
            it.copy(queryForSearch = it.places.joinToString(", ") + "+in+${it.city}")
        }
        println("qqq SearchViewModel->updateQueryForSearch->${_state.value.queryForSearch}")
    }
}