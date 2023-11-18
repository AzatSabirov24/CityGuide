package com.asabirov.search.presentation.map.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.R
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.map.PlaceClusterItem
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapPlacesScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val places = viewModel.placesState.places
    val cameraPositionState = rememberCameraPositionState {
        places.forEach { place ->
            position = CameraPosition.fromLatLngZoom(
                LatLng(place.location.lat, place.location.lng),
                11f
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true)
        ) {
            val items = remember { mutableStateListOf<PlaceClusterItem>() }
            val scope = rememberCoroutineScope()
            LaunchedEffect(places.size) {
                places.forEach {
                    val position = LatLng(
                        it.location.lat,
                        it.location.lng,
                    )
                    items.add(PlaceClusterItem(position, it.name, "", 1f))
                }
            }
            Clustering(
                items = items,
                onClusterClick = { cluster ->
                    val builder = LatLngBounds.builder()
                    for (item in cluster.items) {
                        builder.include(item.position)
                    }
                    val bounds = builder.build()
                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngBounds(bounds, 50),
                            1000
                        )
                    }
                    true
                },
                onClusterItemClick = {
                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    it.itemPosition.latitude,
                                    it.itemPosition.longitude
                                ), 18f
                            ),
                            1000
                        )
                    }
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
                clusterItemContent = {
                    Marker(it.itemTitle)
                }
            )
        }
        Button(
            modifier = Modifier.padding(10.dp),
            onClick = {
                viewModel.onEvent(SearchEvent.OnDownloadMorePlaces)
            }
        ) {
            Text(text = stringResource(id = R.string.download_more))
        }
        if (viewModel.searchState.isSearching) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}