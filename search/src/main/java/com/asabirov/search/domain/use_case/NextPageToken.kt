package com.asabirov.search.domain.use_case

import com.asabirov.search.domain.repository.SearchRepository

class NextPageToken(private val repository: SearchRepository) {

    suspend operator fun invoke(
        query: String,
        nextPageToken: String?
    ) = repository.nextPageToken(query, nextPageToken)
}