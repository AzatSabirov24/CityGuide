package com.asabirov.search.data.remote.dto.place

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AddressComponentDto(
    @SerializedName("long_name")
    val longName: String?,
    @SerializedName("short_name")
    val shortName: String?,
    val types: List<String>
)