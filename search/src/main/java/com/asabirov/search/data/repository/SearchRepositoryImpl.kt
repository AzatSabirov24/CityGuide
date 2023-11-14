package com.asabirov.search.data.repository

import com.asabirov.search.data.mapper.toPlaceDetailsModel
import com.asabirov.search.data.mapper.toPlacesModel
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.PlacesModel
import com.asabirov.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val api: GoogleMapsApi
) : SearchRepository {

    override suspend fun places(query: String): Result<PlacesModel> {
        return try {
            val placesDto = api.places(query)
            Result.success(placesDto).map { it.toPlacesModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun placeDetails(id: String): Result<PlaceDetailsModel> {
        return try {
            val placeDetailsDto = api.placeDetails(id)
            println("qqq SearchRepositoryImpl->placeDetails->${placeDetailsDto}")
            Result.success(placeDetailsDto).map { it.toPlaceDetailsModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}