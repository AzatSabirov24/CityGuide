package com.asabirov.search.data.remote.dto.place

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PhotoDto(
    val height: Int,
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    val photoReference: String,
    val width: Int
)