package com.asabirov.search.data.remote.dto.places

import androidx.annotation.Keep

@Keep
data class LocationDto(
    val lat: Double,
    val lng: Double
)