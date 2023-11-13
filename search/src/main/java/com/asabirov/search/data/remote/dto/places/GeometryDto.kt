package com.asabirov.search.data.remote.dto.places

import androidx.annotation.Keep

@Keep
data class GeometryDto(
    val location: LocationDto,
    val viewport: ViewportDto
)