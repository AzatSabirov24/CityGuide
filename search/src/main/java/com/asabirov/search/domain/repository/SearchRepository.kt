package com.asabirov.search.domain.repository

import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.PlacesModel

interface SearchRepository {

    suspend fun places(
        query: String
    ): Result<PlacesModel>

    suspend fun placeDetails(
        id: String
    ): Result<PlaceDetailsModel>
}