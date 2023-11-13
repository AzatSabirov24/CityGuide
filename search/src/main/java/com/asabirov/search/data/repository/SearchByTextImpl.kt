package com.asabirov.search.data.repository

import com.asabirov.search.data.mapper.toSearchByTextModel
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.domain.model.places.SearchByTextModel
import com.asabirov.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val api: GoogleMapsApi
) : SearchRepository {

    override suspend fun searchByText(query: String): Result<SearchByTextModel> {
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