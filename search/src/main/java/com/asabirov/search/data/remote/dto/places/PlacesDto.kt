package com.asabirov.search.data.remote.dto.places

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PlacesDto(
    val results: List<PlaceDto>,
    val status: String,
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    val nextPageToken: String?
)