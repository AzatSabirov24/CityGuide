package com.asabirov.search.data.remote.dto.search_by_text

import androidx.annotation.Keep

@Keep
data class GeometryDto(
    val location: LocationDto,
    val viewport: ViewportDto
)