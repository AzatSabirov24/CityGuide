package com.asabirov.search.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val locations = mutableListOf<LatLng>()
    viewModel.placesState.places.forEach {
        locations.add(LatLng(it.location.lat, it.location.lng))
    }
    val cameraPositionState = rememberCameraPositionState {
        locations.forEach {
            position = CameraPosition.fromLatLngZoom(it, 10f)
        }

    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        locations.forEach {
            Marker(
                state = MarkerState(position = it),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }
}





