package com.asabirov.search.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.asabirov.search.data.mapper.toPlacesModel
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.domain.model.places.PlaceModel
import kotlinx.coroutines.delay

class PlacesPagingSource(
    private val api: GoogleMapsApi,
    private val query: String
) : PagingSource<String, PlaceModel>() {

    override fun getRefreshKey(state: PagingState<String, PlaceModel>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PlaceModel> {
        return try {
            val response = api.places(
                query = query,
                nextPageToken = params.key
            )
            println("qqq PlacesPagingSource->load->${params.key}")
            val mappedResponse = response.toPlacesModel()
            delay(2000)
            LoadResult.Page(
                data = mappedResponse.places,
                prevKey = null,
                nextKey = mappedResponse.nextPageToken
            )
        } catch (e: Exception) {
            println("qqq PlacesPagingSource->Exception->${e.message}")
            LoadResult.Error(e)
        }
    }
}