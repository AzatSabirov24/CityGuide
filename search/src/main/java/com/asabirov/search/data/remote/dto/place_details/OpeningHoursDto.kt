package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OpeningHoursDto(
    @SerializedName("open_now")
    val openNow: Boolean,
    val periods: List<PeriodDto>,
    @SerializedName("weekday_text")
    val weekdayText: List<String>
)