package com.asabirov.search.data.mapper

import com.asabirov.search.BuildConfig
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.data.remote.dto.place_details.PhotoDto
import com.asabirov.search.data.remote.dto.place_details.PlaceDetailsDto
import com.asabirov.search.data.remote.dto.place_details.PlaceDetailsLocationDto
import com.asabirov.search.data.remote.dto.places.LocationDto
import com.asabirov.search.data.remote.dto.places.PlaceDto
import com.asabirov.search.data.remote.dto.places.PlacesDto
import com.asabirov.search.domain.model.place_details.PhotoModel
import com.asabirov.search.domain.model.place_details.PlaceDetailsLocationModel
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.LocationModel
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.model.places.PlacesModel

fun PlaceDto.toPlaceModel(): PlaceModel {
    return PlaceModel(
        id = placeId,
        name = name,
        photoUrl = setPhotoUrlWithQuery(photos?.firstOrNull()?.photoReference),
        location = geometry.location.toLocationModel(),
        isOpenNow = openingHours?.openNow,
        rating = rating
    )
}

fun PlacesDto.toPlacesModel(): PlacesModel {
//    val resultsWithPhoto = this.results.filter { it.photos != null }
    val results = this.results.map { it.toPlaceModel() }
    return PlacesModel(
        nextPageToken = nextPageToken,
        places = results
    )
}

fun LocationDto.toLocationModel(): LocationModel = LocationModel(lat = lat, lng = lng)

fun PlaceDetailsDto.toPlaceDetailsModel(): PlaceDetailsModel {
    return PlaceDetailsModel(
        id = result.placeId,
        name = result.name,
        openingTime = result.currentOpeningHours?.weekdayText,
        address = result.formattedAddress,
        phoneNumber = result.internationalPhoneNumber,
        location = result.geometry.location.toPlaceDetailsLocationModel(),
        photos = result.photos?.toPhotoModels(),
        rating = result.rating
    )
}

fun PlaceDetailsLocationDto.toPlaceDetailsLocationModel() =
    PlaceDetailsLocationModel(lat = lat, lng = lng)

fun List<PhotoDto>.toPhotoModels(): List<PhotoModel> {
    val photos = mutableListOf<PhotoModel>()
    photos.addAll(this.map {
        PhotoModel(url = setPhotoUrlWithQuery(it.photoReference))
    })
    return photos
}

fun setPhotoUrlWithQuery(photoReference: String?): String {
    var photoUrlWithQuery = ""
    val photoEndPoint = "api/place/photo"
    photoReference?.let {
        photoUrlWithQuery =
            "${GoogleMapsApi.BASE_URL}$photoEndPoint?maxwidth=400&photoreference=$it&key=${BuildConfig.MAPS_API_KEY}"
    }
    return photoUrlWithQuery
}