package com.asabirov.search.data.remote.dto.place

import androidx.annotation.Keep

@Keep
data class ViewportDto(
    val northeastDto: NortheastDto,
    val southwestDto: SouthwestDto
)