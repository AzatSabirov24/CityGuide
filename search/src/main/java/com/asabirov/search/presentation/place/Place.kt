package com.asabirov.search.presentation.place

sealed class Place(val name: String) {

    data object Museum: Place("museum")
    data object Cinema: Place("cinema")
    data object Empty: Place("")

    companion object {

        fun fromString(name: String): Place {
           return when(name) {
               "museum" -> Museum
               "cinema" -> Cinema
               else -> Empty
           }
        }
    }
}
