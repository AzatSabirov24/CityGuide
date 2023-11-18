package com.asabirov.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.asabirov.search.data.mapper.getNextPageToken
import com.asabirov.search.data.mapper.toPlaceDetailsModel
import com.asabirov.search.data.mapper.toPlacesModel
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.data.remote.paging.PlacesPagingSource
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.model.places.PlacesModel
import com.asabirov.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val api: GoogleMapsApi
) : SearchRepository {

    override fun placesPaginated(
        query: String,
        nextPageToken: String?
    ): Pager<String, PlaceModel> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PlacesPagingSource(
                    api = api,
                    query = query
                )
            }
        )
    }

    override suspend fun places(
        query: String,
        nextPageToken: String?
    ): Result<PlacesModel> {
        return try {
            val placesDto = api.places(query, nextPageToken)
            Result.success(placesDto).map { it.toPlacesModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun nextPageToken(query: String, nextPageToken: String?): String {
        return try {
            val placesDto = api.places(query, nextPageToken)
            placesDto.getNextPageToken()
        } catch (e: Exception) {
            e.message.toString()
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