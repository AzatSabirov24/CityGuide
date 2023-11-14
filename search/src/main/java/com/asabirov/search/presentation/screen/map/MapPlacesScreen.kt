package com.asabirov.search.presentation.screen.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapPlacesScreen(
    viewModel: SearchViewModel = hiltViewModel(),
) {
    var selectPlace by remember {
        mutableStateOf(SelectedPlace())
    }
    val places = viewModel.placesState.places
    val cameraPositionState = rememberCameraPositionState {
        places.forEach { place ->
            position = CameraPosition.fromLatLngZoom(
                LatLng(place.location.lat, place.location.lng),
                11f
            )
        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        val items = remember { mutableStateListOf<PlaceClusterItem>() }
        LaunchedEffect(Unit) {
            places.forEach {
                val position = LatLng(
                    it.location.lat,
                    it.location.lng,
                )
                items.add(PlaceClusterItem(position, it.name, "", 5f))
            }
        }
        LaunchedEffect(key1 = selectPlace) {
            selectPlace.itemPosition?.let {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        it,
                        16.0f
                    ),
                    1000
                )
            }

        }
        Clustering(
            items = items,
            onClusterClick = {
                cameraPositionState.position = CameraPosition(it.position, 13f, 1f, 1f)
                true
            },
            onClusterItemClick = {
                selectPlace =
                    selectPlace.copy(
                        itemPosition = LatLng(
                            it.itemPosition.latitude,
                            it.itemPosition.longitude
                        )
                    )
                true
            },
            clusterContent = { cluster ->
                Surface(
                    Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.Blue,
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.Green)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "%,d".format(cluster.size),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            // Optional: Custom rendering for non-clustered items
            clusterItemContent = null
        )
    }
}

data class SelectedPlace(
    val itemPosition: LatLng? = null
)




