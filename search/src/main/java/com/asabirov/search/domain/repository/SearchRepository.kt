package com.asabirov.search.domain.repository

import com.asabirov.search.domain.model.places.SearchByTextModel

interface SearchRepository {

    suspend fun searchByText(
        query: String
    ): Result<SearchByTextModel>
}