package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep

@Keep
data class OpenDto(
    val date: String,
    val day: Int,
    val time: String
)