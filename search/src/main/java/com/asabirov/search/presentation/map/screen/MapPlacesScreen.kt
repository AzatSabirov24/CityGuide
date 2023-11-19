package com.asabirov.search.presentation.map.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.core_ui.event.UiEvent
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.search.R
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.map.PlaceClusterItem
import com.asabirov.search.presentation.search.screen.PlaceItem
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapPlacesScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    openPlaceDetails: () -> Unit
) {
    val context = LocalContext.current
    val places = viewModel.placesState.places.distinctBy { it.id }
    val snackbarHostState = remember { SnackbarHostState() }
    val scopeUiEvent = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        places.forEach { place ->
            position = CameraPosition.fromLatLngZoom(
                LatLng(place.location.lat, place.location.lng),
                11f
            )
        }
    }
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val spacing = LocalSpacing.current
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp)
                ) {
                    Text(data.visuals.message)
                }
            }
        }
    ) {
        scopeUiEvent.launch {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }

                    else -> {}
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
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
                        items.add(
                            PlaceClusterItem(
                                itemPosition = position,
                                itemTitle = it.name,
                                itemSnippet = "",
                                itemZIndex = 1f,
                                name = it.name,
                                id = it.id,
                                photoUrl = it.photoUrl,
                                location = it.location,
                                isOpenNow = it.isOpenNow,
                                rating = it.rating,
                            )
                        )
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
                            viewModel.onEvent(
                                SearchEvent.OnSelectPlaceOnMap(
                                    placeModel =
                                    PlaceModel(
                                        id = it.id,
                                        name = it.name,
                                        photoUrl = it.photoUrl,
                                        location = it.location,
                                        isOpenNow = it.isOpenNow,
                                        rating = it.rating
                                    )
                                )
                            )
                            isSheetOpen = true
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
        if (isSheetOpen) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isSheetOpen = false }) {
                val placeModel = viewModel.placesState.selectedPlaceModel
                placeModel?.let { place ->
                    PlaceItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(spacing.spaceSmall),
                        place = PlaceModel(
                            id = place.id,
                            name = place.name,
                            photoUrl = place.photoUrl,
                            location = place.location,
                            isOpenNow = place.isOpenNow,
                            rating = place.rating
                        ),
                        onClick = { placeId ->
                            isSheetOpen = false
                            viewModel.onEvent(SearchEvent.OnSelectPlace(id = placeId))
                            openPlaceDetails()
                        },
                        color = Color.Gray
                    )
                }
            }
        }
    }
}