package com.asabirov.search.domain.use_case

import androidx.paging.Pager
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.repository.SearchRepository

class SearchPlaces(private val repository: SearchRepository) {

//    suspend operator fun invoke(
//        query: String,
//        nextPageToken: String?
//    ): Result<PlacesModel> {
//        return repository.places(
//            query = query,
//            nextPageToken
//        )
//    }

    operator fun invoke(
        query: String,
        nextPageToken: String?
    ): Pager<String, PlaceModel> {
        return repository.placesPaginated(
            query = query,
            nextPageToken = nextPageToken
        )
    }
}
