package com.asabirov.search.domain.use_case

import com.asabirov.search.domain.model.places.PlacesModel
import com.asabirov.search.domain.repository.SearchRepository

class SearchPlaces(private val repository: SearchRepository) {

    suspend operator fun invoke(
        query: String,
        nextPageToken: String?
    ): Result<PlacesModel> {
        return repository.places(
            query = query,
            nextPageToken
        )
    }
}
