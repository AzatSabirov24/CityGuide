package com.asabirov.search.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asabirov.search.domain.use_case.SearchUseCases
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.place.Query
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

    var state by mutableStateOf(SearchState())
        private set

    private val _cityQuery = MutableStateFlow(Query.CityQuery())
    val cityQuery = _cityQuery.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> {
                executeSearch()
            }

            is SearchEvent.OnAddCityName -> {
                state = state.copy(cityName = event.name)
                println("qqq SearchViewModel->onEvent->${state.cityName}")
            }

            is SearchEvent.OnAddQuery -> {
                if (state.query.contains(event.query)) return
                else {
                    state = state.copy(query = state.query + event.query)
                    println("qqq SearchViewModel->OnAddQuery->${state.query}")
                }
            }

            is SearchEvent.OnRemoveQuery -> {
                state = state.copy(query = state.query.replace(event.query, ""))
                println("qqq SearchViewModel->OnRemoveQuery->${state.query}")
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            state = state.copy(
                isSearching = true
            )
            searchUseCases.searchByText(state.query)
                .onSuccess { searchResult ->
                    println("qqq SearchViewModel->onSuccess->${searchResult.results}")
                    state = state.copy(
                        isSearching = false,
                        searchResult = searchResult,
                    )
                }
                .onFailure { println("qqq SearchViewModel->onFailure->${it.message}") }
        }
    }

    fun updateCityQuery(cityName: String) {
        _cityQuery.update {
            it.copy(cityName = cityName)
        }
    }
}