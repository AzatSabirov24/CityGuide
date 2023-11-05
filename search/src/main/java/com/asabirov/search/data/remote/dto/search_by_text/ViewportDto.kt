package com.asabirov.search.data.remote.dto.search_by_text

import androidx.annotation.Keep

@Keep
data class ViewportDto(
    val northeast: NortheastDto,
    val southwest: SouthwestDto
)