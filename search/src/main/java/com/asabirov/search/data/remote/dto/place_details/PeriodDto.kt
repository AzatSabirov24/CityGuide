package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep

@Keep
data class PeriodDto(
    val close: CloseDto,
    val open: OpenDto
)