package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep

@Keep
data class GeometryDto(
    val location: PlaceDetailsLocationDto,
    val viewport: ViewportDto
)