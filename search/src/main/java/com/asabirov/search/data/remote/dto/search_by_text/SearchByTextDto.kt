package com.asabirov.search.data.remote.dto.search_by_text

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SearchByTextDto(
    val results: List<ResultDto>,
    val status: String,
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    val nextPageToken: String
)