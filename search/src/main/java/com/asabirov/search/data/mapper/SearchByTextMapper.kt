package com.asabirov.search.data.mapper

import com.asabirov.search.BuildConfig
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.data.remote.dto.search_by_text.LocationDto
import com.asabirov.search.data.remote.dto.search_by_text.ResultDto
import com.asabirov.search.data.remote.dto.search_by_text.SearchByTextDto
import com.asabirov.search.domain.model.search_by_text.LocationModel
import com.asabirov.search.domain.model.search_by_text.PlaceModel
import com.asabirov.search.domain.model.search_by_text.SearchByTextModel

fun ResultDto.toResultModel(): PlaceModel {
    return PlaceModel(
        name = name,
        photoUrl = setPhotoUrlWithQuery(this),
        location = geometry.location.toLocationModel(),
        isOpenNow = openingHours?.openNow
    )
}

fun SearchByTextDto.toSearchByTextModel(): SearchByTextModel {
//    val resultsWithPhoto = this.results.filter { it.photos != null }
    val results = this.results.map { it.toResultModel() }
    return SearchByTextModel(results = results)
}

fun LocationDto.toLocationModel(): LocationModel = LocationModel(lat = lat, lng = lng)

fun setPhotoUrlWithQuery(resultDto: ResultDto?): String {
    val photoReference = resultDto?.photos?.firstOrNull()?.photoReference
    var photoUrlWithQuery = ""
    val photoEndPoint = "api/place/photo"
    photoReference?.let {
        photoUrlWithQuery =
            "${GoogleMapsApi.BASE_URL}$photoEndPoint?maxwidth=400&photoreference=$it&key=${BuildConfig.MAPS_API_KEY}"
    }
    return photoUrlWithQuery
}