package com.asabirov.search.data.remote.dto.place_details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResultDto(
    @SerializedName("place_id")
    val placeId: String,
    val name: String,
    @SerializedName("address_components")
    val addressComponents: List<AddressComponentDto>,
    @SerializedName("adr_address")
    val adrAddress: String,
    @SerializedName("business_status")
    val businessStatus: String,
    @SerializedName("current_opening_hours")
    val currentOpeningHours: CurrentOpeningHoursDto?,
    @SerializedName("formatted_address")
    val formattedAddress: String,
    @SerializedName("formatted_phone_number")
    val formattedPhoneNumber: String,
    val geometry: GeometryDto,
    val icon: String,
    @SerializedName("icon_background_color")
    val iconBackgroundColor: String,
    @SerializedName("icon_mask_base_uri")
    val iconMaskBaseUri: String,
    @SerializedName("international_phone_number")
    val internationalPhoneNumber: String,
    @SerializedName("opening_hours")
    val openingHours: OpeningHoursDto,
    val photos: List<PhotoDto>?,
    @SerializedName("plus_code")
    val plusCode: PlusCodeDto,
    val rating: Double,
    val reference: String,
    val reviews: List<ReviewDto>,
    val types: List<String>,
    val url: String,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    @SerializedName("utc_offset")
    val utcOffset: Int,
    val vicinity: String,
    val website: String,
    @SerializedName("wheelchair_accessible_entrance")
    val wheelchairAccessibleEntrance: Boolean
)