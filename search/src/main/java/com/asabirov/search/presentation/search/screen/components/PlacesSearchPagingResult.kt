package com.asabirov.search.presentation.search.screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.asabirov.search.R
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun PlacesSearchPagingResult(
    places: LazyPagingItems<PlaceModel>?,
    lazyListState: LazyListState,
    viewModel: SearchViewModel = hiltViewModel(),
    openPlaceDetails: () -> Unit,
    navigateToMap: () -> Unit,
    locationPermission: PermissionState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        places?.let { pagingItems ->
            if (pagingItems.itemCount != 0) {
                Column {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        state = lazyListState,
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
                    ) {
                        viewModel.onEvent(SearchEvent.OnAddPlaceToState(pagingItems.itemSnapshotList.items))
                        items(pagingItems) { place ->
                            if (place != null) {
                                PlaceItem(
                                    modifier = Modifier.padding(4.dp),
                                    place = place,
                                    onClick = {
                                        viewModel.onEvent(
                                            SearchEvent.OnSelectPlace(
                                                place.id
                                            )
                                        )
                                        openPlaceDetails()
                                    }
                                )
                            }
                        }
                        item {
                            if (pagingItems.loadState.append is LoadState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(top = 100.dp)
                                )
                            }
                        }
                    }
                    Button(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        onClick = {
                            if (locationPermission.status.isGranted) {
                                navigateToMap()
                            } else
                                viewModel.showSnackBar()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.on_map))
                    }
                }
            }
        }
    }
}