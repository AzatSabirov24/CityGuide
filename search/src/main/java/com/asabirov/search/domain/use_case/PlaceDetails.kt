package com.asabirov.search.domain.use_case

import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.repository.SearchRepository

class PlaceDetails(private val repository: SearchRepository) {

    suspend operator fun invoke(id: String): Result<PlaceDetailsModel> {
        return repository.placeDetails(id = id)
    }
}