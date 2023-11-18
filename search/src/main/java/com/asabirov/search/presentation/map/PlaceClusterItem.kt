package com.asabirov.search.presentation.map

import com.asabirov.search.domain.model.places.LocationModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class PlaceClusterItem(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val itemZIndex: Float,
    val id: String,
    val name: String,
    val photoUrl: String,
    val location: LocationModel,
    val isOpenNow: Boolean?,
    val rating: Double?
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet

    override fun getZIndex(): Float =
        itemZIndex
}
