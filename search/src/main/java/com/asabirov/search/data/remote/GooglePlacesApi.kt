package com.asabirov.search.data.remote

import com.asabirov.search.data.remote.dto.places.PlacesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {

    @GET("api/place/textsearch/json")
    suspend fun places(
        @Query("query") query: String
    ): PlacesDto

    @GET("api/place/details/json")
    suspend fun placeDetails(
        @Query("placeid") id: String
    ): PlacesDto

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/"
    }
}