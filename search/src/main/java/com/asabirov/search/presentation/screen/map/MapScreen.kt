package com.asabirov.search.presentation.screen.map

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
    val places = viewModel.placesState.places
    val cameraPositionState = rememberCameraPositionState {
        places.forEach { place ->
            position =
                CameraPosition.fromLatLngZoom(
                    LatLng(place.location.lat, place.location.lng),
                    10f
                )
        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        places.forEach { place ->
            Marker(
                state = MarkerState(position = LatLng(place.location.lat, place.location.lng)),
                title = place.name
            )
        }
    }
}





