package com.asabirov.search.domain.use_case

import com.asabirov.search.domain.model.places.SearchByTextModel
import com.asabirov.search.domain.repository.SearchRepository

class SearchByText(private val repository: SearchRepository) {

    suspend operator fun invoke(query: String): Result<SearchByTextModel>{
        return repository.searchByText(query)
    }
}
