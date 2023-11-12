package com.asabirov.search.data.remote.dto.search_by_text

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResultDto(
    val name: String,
    val photos: List<PhotoDto>?,
    val icon: String,
    val rating: Double,
    val reference: String,
    val types: List<String>,
    val geometry: GeometryDto,
    @SerializedName("business_status")
    val businessStatus: String,
    @SerializedName("formatted_address")
    val formattedAddress: String,
    @SerializedName("icon_background_color")
    val iconBackgroundColor: String,
    @SerializedName("icon_mask_base_uri")
    val iconMaskBaseUri: String,
    @SerializedName("opening_hours")
    val openingHours: OpeningHoursDto,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("plus_code")
    val plusCode: PlusCodeDto,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int
)