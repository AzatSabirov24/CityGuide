package com.asabirov.search.presentation.screen.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPlaceDetailsScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    var selectPlace by remember {
        mutableStateOf(SelectedPlace())
    }
    val place = viewModel.placeDetailsState.place
    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(
                LatLng(place.location?.lat ?: 0.0, place.location?.lng ?: 0.0),
                12f
            )
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        LaunchedEffect(key1 = selectPlace) {
            selectPlace.itemPosition?.let {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        it,
                        18.0f
                    ),
                    1000
                )
            }
        }
        Marker(
            state = MarkerState(
                position = LatLng(
                    place.location?.lat ?: 0.0,
                    place.location?.lng ?: 0.0
                )
            ),
            title = place.name,
            onClick = {
                selectPlace = selectPlace.copy(
                    itemPosition = LatLng(
                        it.position.latitude,
                        it.position.longitude
                    )
                )
                true
            }
        )
    }
}