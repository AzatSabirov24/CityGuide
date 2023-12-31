package com.asabirov.search.presentation.map.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MapPlaceDetailsScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    var isMarkerClicked by remember {
        mutableStateOf(false)
    }
    val place by remember {
        mutableStateOf(viewModel.placeDetailsState.place)
    }
    val placeLatLng by remember {
        mutableStateOf(LatLng(place.location?.lat ?: 0.0, place.location?.lng ?: 0.0))
    }
    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(
                placeLatLng,
                12f
            )
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        val scope = rememberCoroutineScope()
        if (isMarkerClicked) {
            scope.launch {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        placeLatLng,
                        18.0f
                    ),
                    1000
                )
                isMarkerClicked = false
            }
        }
        val markerState = rememberMarkerState(
            position = placeLatLng
        )
        MarkerComposable(
            state = markerState,
            onClick = {
                isMarkerClicked = true
                true
            }
        ) {
            Marker(text = place.name ?: "")
        }
    }
}