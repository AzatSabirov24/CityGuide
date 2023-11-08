package com.asabirov.search.presentation.place

sealed interface Query {

    data class CityQuery(val cityName: String = ""): Query
}
