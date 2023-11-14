package com.asabirov.search.presentation.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        val markerState = rememberMarkerState(
            position = LatLng(
                place.location?.lat ?: 0.0,
                place.location?.lng ?: 0.0
            )
        )
        MarkerComposable(
            state = markerState,
            onClick = {
                selectPlace = selectPlace.copy(
                    itemPosition = LatLng(
                        it.position.latitude,
                        it.position.longitude
                    )
                )
                true
            }
        ) {
            Marker(place.name ?: "")
        }
    }
}

@Composable
fun Marker(text: String) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                2.dp,
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF065C74),
                        Color(0xFF06C7B2)
                    )
                )
            )
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )
    }
}