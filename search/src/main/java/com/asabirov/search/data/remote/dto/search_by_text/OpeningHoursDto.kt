package com.asabirov.search.data.remote.dto.search_by_text

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OpeningHoursDto(
    @SerializedName("open_now")
    val openNow: Boolean
)