package com.asabirov.search.data.remote.dto.place

import androidx.annotation.Keep

@Keep
data class PeriodDto(
    val closeDto: CloseDto,
    val openDto: OpenDto
)