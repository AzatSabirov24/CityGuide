package com.asabirov.core.utils.location

import android.content.Context
import android.location.Geocoder
import android.os.Build.VERSION_CODES.TIRAMISU
import com.google.android.datatransport.runtime.BuildConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationService(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getCurrentCity(
        hasLocationPermission: Boolean,
        callback: (String?) -> Unit
    ) {
        if (!hasLocationPermission) return
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location == null) return@addOnSuccessListener
                val latLng = LatLng(location.latitude, location.longitude)
                val geocoder = Geocoder(context, Locale.getDefault())
                if (BuildConfig.VERSION_CODE >= TIRAMISU) {
                    geocoder.getFromLocation(
                        latLng.latitude, latLng.longitude, 1
                    ) { addresses ->
                        if (addresses.isNotEmpty()) {
                            callback(addresses.firstOrNull()?.locality)
                        }
                    }
                } else {
                    val addresses =
                        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    if (addresses?.isNotEmpty() == true) {
                        callback(addresses.firstOrNull()?.locality)
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                callback(null)
            }
    }
}
