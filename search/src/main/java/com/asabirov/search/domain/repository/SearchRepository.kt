package com.asabirov.search.domain.repository

import com.asabirov.search.domain.model.places.PlacesModel

interface SearchRepository {

    suspend fun places(
        query: String
    ): Result<PlacesModel>
}