package com.asabirov.search.domain.repository

import com.asabirov.search.domain.model.search_by_text.SearchByTextModel

interface SearchRepository {

    suspend fun searchByText(
        query: String
    ): Result<SearchByTextModel>
}