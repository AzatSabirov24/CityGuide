package com.asabirov.search.data.remote.dto.place

import androidx.annotation.Keep

@Keep
data class GeometryDto(
    val locationDto: LocationDto,
    val viewportDto: ViewportDto
)