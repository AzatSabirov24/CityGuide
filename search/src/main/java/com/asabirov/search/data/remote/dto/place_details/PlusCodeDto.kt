package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PlusCodeDto(
    @SerializedName("compound_code")
    val compoundCode: String,
    @SerializedName("global_code")
    val globalCode: String
)