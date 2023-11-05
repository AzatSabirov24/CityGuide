package com.asabirov.search.data.remote

import com.asabirov.search.data.remote.dto.search_by_text.SearchByTextDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {

    @GET("api/place/textsearch/json")
    suspend fun searchByText(
        @Query("query") query: String
    ): SearchByTextDto

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/"
    }
}