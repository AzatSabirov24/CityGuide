package com.asabirov.search.data.remote.dto.place

data class PlaceDetailsDto(
    val html_attributions: List<Any>,
    val resultDto: ResultDto,
    val status: String
)