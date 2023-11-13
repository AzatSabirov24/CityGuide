package com.asabirov.search.data.repository

import com.asabirov.search.data.mapper.toSearchByTextModel
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.domain.model.places.PlacesModel
import com.asabirov.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val api: GoogleMapsApi
) : SearchRepository {

    override suspend fun places(query: String): Result<PlacesModel> {
        return try {
            val searchByTextDto = api.places(query)
            Result.success(
                searchByTextDto
            ).map { it.toSearchByTextModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}