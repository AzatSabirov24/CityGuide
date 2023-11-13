package com.asabirov.search.data.remote.dto.places

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OpeningHoursDto(
    @SerializedName("open_now")
    val openNow: Boolean
)