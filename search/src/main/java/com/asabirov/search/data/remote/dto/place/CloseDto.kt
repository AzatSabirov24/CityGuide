package com.asabirov.search.data.remote.dto.place

import androidx.annotation.Keep

@Keep
data class CloseDto(
    val date: String?,
    val day: Int?,
    val time: String?
)