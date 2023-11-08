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
                println("qqq SearchViewModel->onEvent->${_state.value.city}")
            }

            is SearchEvent.OnChangePlace -> {

            }

//            is SearchEvent.OnAddQuery -> {
//                if (state.query.contains(event.query)) return
//                else {
//                    state = state.copy(query = state.query + event.query)
//                    println("qqq SearchViewModel->OnAddQuery->${state.query}")
//                }
//            }
//
//            is SearchEvent.OnRemoveQuery -> {
//                state = state.copy(query = state.query.replace(event.query, ""))
//                println("qqq SearchViewModel->OnRemoveQuery->${state.query}")
//            }
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
}