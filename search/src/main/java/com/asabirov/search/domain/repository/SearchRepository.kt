package com.asabirov.search.domain.repository

import androidx.paging.Pager
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.model.places.PlacesModel

interface SearchRepository {

    suspend fun places(
        query: String,
        nextPageToken: String?
    ): Result<PlacesModel>

     fun placesPaginated(
        query: String,
        nextPageToken: String?
    ): Pager<String, PlaceModel>

    suspend fun placeDetails(
        id: String
    ): Result<PlaceDetailsModel>
}