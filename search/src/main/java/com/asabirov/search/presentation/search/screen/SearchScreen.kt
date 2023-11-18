package com.asabirov.search.presentation.search.screen

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.asabirov.core.utils.event.UiEvent
import com.asabirov.core.utils.location.LocationService
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.search.R
import com.asabirov.search.presentation.components.SearchTextField
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class
)
@Composable
fun SearchScreen(
    navigateToMap: () -> Unit,
    openPlaceDetails: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locationService = LocationService(context)
    val keyboardController = LocalSoftwareKeyboardController.current
    var isHideKeyboard by remember { mutableStateOf(false) }
    val hideKeyboard = {
        keyboardController?.hide()
        isHideKeyboard = true
    }
    val spacing = LocalSpacing.current
    val searchState = viewModel.searchState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val locationPermission = rememberPermissionState(
        permission = ACCESS_COARSE_LOCATION
    )
    val places = viewModel.searchPagingFlow?.collectAsLazyPagingItems()
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp),
                    action = {
                        TextButton(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                val i = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", context.packageName, null)
                                i.data = uri
                                context.startActivity(i)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) { Text(stringResource(id = R.string.go_to_settings)) }
                    }
                ) {
                    Text(data.visuals.message)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            scope.launch {
                viewModel.uiEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowSnackbar -> {
                            snackbarHostState.showSnackbar(
                                message = event.message.asString(context)
                            )
                            keyboardController?.hide()
                        }

                        else -> {}
                    }
                }
            }
            SearchTextField(
                text = searchState.city,
                onValueChange = { cityName ->
                    isHideKeyboard = false
                    viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = cityName))
                },
                onSearch = {
                    hideKeyboard()
                    viewModel.onEvent(SearchEvent.OnSearch)
                },
                iconLeftRequired = true,
                iconLeft = {
                    IconButton(
                        onClick = {
                            if (locationPermission.status.isGranted) {
                                locationService.getCurrentCity(locationService.hasLocationPermission()) { cityName ->
                                    viewModel.onEvent(
                                        SearchEvent.OnChangeCityName(
                                            cityName = cityName ?: ""
                                        )
                                    )
                                }
                            } else
                                viewModel.showSnackBar()
                            isHideKeyboard = false
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = stringResource(id = R.string.current_location)
                        )
                    }
                },
                iconRightRequired = true,
                iconRight = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = ""))
                            isHideKeyboard = false
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }
                },
                hideKeyboard = isHideKeyboard,
                label = stringResource(id = R.string.city_label),
                onFocusChanged = { cityName ->
                    viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = cityName))
                },
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            SearchTextField(
                text = searchState.placesNames.joinToString(" "),
                onValueChange = {
                    isHideKeyboard = false
                    viewModel.onEvent(SearchEvent.OnAddPlaceByEditTextField(placeName = it))
                },
                onSearch = {
                    hideKeyboard()
                    viewModel.onEvent(SearchEvent.OnSearch)
                },
                iconRightRequired = true,
                iconRight = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(SearchEvent.OnRemoveAllPlaces)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }
                },
                label = stringResource(id = R.string.place_label),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                FlowRow {
                    SetPlace(placeName = "Restaurants", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Cafe", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Museums", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Cinemas", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Shopping malls", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Universities", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Hospitals", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Fast food", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Night Clubs", hideKeyboard = { hideKeyboard() })
                    SetPlace(placeName = "Hookah places", hideKeyboard = { hideKeyboard() })
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Button(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                onClick = {
                    hideKeyboard()
                    viewModel.onEvent(SearchEvent.OnSearch)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                places?.let {
                    if (it.loadState.refresh is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        Column {
                            LazyRow(modifier = Modifier.fillMaxWidth()) {
                                viewModel.onEvent(SearchEvent.OnAddPlaceToState(it.itemSnapshotList.items))
                                items(it) { place ->
                                    if (place != null) {
                                        PlaceItem(
                                            modifier = Modifier.padding(4.dp),
                                            place = place,
                                            onClick = {
                                                viewModel.onEvent(SearchEvent.OnSelectPlace(place.id))
                                                openPlaceDetails()
                                            }
                                        )
                                    }
                                }
                                item {
                                    if (it.loadState.append is LoadState.Loading) {
                                        CircularProgressIndicator(modifier = Modifier.padding(top = 100.dp))
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
    }
}

@Composable
private fun SetPlace(
    placeName: String,
    viewModel: SearchViewModel = hiltViewModel(),
    hideKeyboard: () -> Unit
) {
    PlaceSelectableButton(
        text = placeName,
        color = MaterialTheme.colorScheme.primary,
        selectedTextColor = Color.White,
        onClick = { isSelected ->
            hideKeyboard()
            if (isSelected) viewModel.onEvent(SearchEvent.OnAddPlaceByClickTag(placeName = placeName))
            else viewModel.onEvent(SearchEvent.OnRemovePlace(placeName = placeName))
        },
        viewModel = viewModel
    )
}