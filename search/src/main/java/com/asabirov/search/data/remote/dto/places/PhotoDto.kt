package com.asabirov.search.data.remote.dto.places

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PhotoDto(
    val height: Int,
    val width: Int,
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    val photoReference: String
)