package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CurrentOpeningHoursDto(
    @SerializedName("open_now")
    val openNow: Boolean,
    val periodDtos: List<PeriodDto>,
    @SerializedName("weekday_text")
    val weekdayText: List<String>
)