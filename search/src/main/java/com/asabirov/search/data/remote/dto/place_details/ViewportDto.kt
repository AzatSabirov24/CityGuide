package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep

@Keep
data class ViewportDto(
    val northeast: NortheastDto,
    val southwest: SouthwestDto
)