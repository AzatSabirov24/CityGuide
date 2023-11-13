package com.asabirov.search.data.mapper

import com.asabirov.search.BuildConfig
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.data.remote.dto.places.LocationDto
import com.asabirov.search.data.remote.dto.places.PlacesDto
import com.asabirov.search.data.remote.dto.places.ResultDto
import com.asabirov.search.domain.model.places.LocationModel
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.model.places.PlacesModel

fun ResultDto.toResultModel(): PlaceModel {
    return PlaceModel(
        id = placeId,
        name = name,
        photoUrl = setPhotoUrlWithQuery(this),
        location = geometry.location.toLocationModel(),
        isOpenNow = openingHours?.openNow,
        rating = rating
    )
}

fun PlacesDto.toSearchByTextModel(): PlacesModel {
//    val resultsWithPhoto = this.results.filter { it.photos != null }
    val results = this.results.map { it.toResultModel() }
    return PlacesModel(places = results)
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