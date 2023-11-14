package com.asabirov.search.data.mapper

import com.asabirov.search.BuildConfig
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.data.remote.dto.place_details.PhotoDto
import com.asabirov.search.data.remote.dto.place_details.PlaceDetailsDto
import com.asabirov.search.data.remote.dto.place_details.PlaceDetailsLocationDto
import com.asabirov.search.data.remote.dto.places.LocationDto
import com.asabirov.search.data.remote.dto.places.PlaceDto
import com.asabirov.search.data.remote.dto.places.PlacesDto
import com.asabirov.search.domain.model.place_details.PlaceDetailsLocationModel
import com.asabirov.search.domain.model.place_details.PlaceDetailsModel
import com.asabirov.search.domain.model.places.LocationModel
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.domain.model.places.PlacesModel

fun PlaceDto.toPlaceModel(): PlaceModel {
    return PlaceModel(
        id = placeId,
        name = name,
        photoUrl = setPhotoUrlWithQuery(this),
        location = geometry.location.toLocationModel(),
        isOpenNow = openingHours?.openNow,
        rating = rating
    )
}

fun PlacesDto.toPlacesModel(): PlacesModel {
//    val resultsWithPhoto = this.results.filter { it.photos != null }
    val results = this.results.map { it.toPlaceModel() }
    return PlacesModel(places = results)
}

fun LocationDto.toLocationModel(): LocationModel = LocationModel(lat = lat, lng = lng)

fun setPhotoUrlWithQuery(placeDto: PlaceDto?): String {
    val photoReference = placeDto?.photos?.firstOrNull()?.photoReference
    var photoUrlWithQuery = ""
    val photoEndPoint = "api/place/photo"
    photoReference?.let {
        photoUrlWithQuery =
            "${GoogleMapsApi.BASE_URL}$photoEndPoint?maxwidth=400&photoreference=$it&key=${BuildConfig.MAPS_API_KEY}"
    }
    return photoUrlWithQuery
}

fun PlaceDetailsDto.toPlaceDetailsModel(): PlaceDetailsModel {
    return PlaceDetailsModel(
        id = resultDto.placeId,
        name = resultDto.name,
        openingTime = resultDto.currentOpeningHours.weekdayText,
        address = resultDto.formattedAddress,
        phoneNumber = resultDto.formattedPhoneNumber,
        location = resultDto.geometryDto.locationDto.toPlaceDetailsLocationModel(),
        photos = resultDto.photoDtos.toPhotos(),
        rating = resultDto.rating
    )
}

fun PlaceDetailsLocationDto.toPlaceDetailsLocationModel() =
    PlaceDetailsLocationModel(lat = lat, lng = lng)

fun List<PhotoDto>.toPhotos(): List<String> {
    val photos = mutableListOf<String>()
    photos.addAll(this.map { it.photoReference })
    return photos
}