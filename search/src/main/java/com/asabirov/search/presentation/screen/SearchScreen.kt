package com.asabirov.search.presentation.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.core.utils.location.LocationService
import com.asabirov.search.R
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.screen.components.PlaceItem
import com.asabirov.search.presentation.screen.components.PlaceSelectableButton
import com.asabirov.search.presentation.screen.components.SearchTextField
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
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
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    locationService.getCurrentCity(locationService.hasLocationPermission()) {
                        viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = it ?: ""))
                    }
                }
            }
        )
    var isLocationPermissionsGranted by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        isLocationPermissionsGranted = locationService.hasLocationPermission()
        val city = remember {
            mutableStateOf("")
        }
        val places = remember {
            mutableStateOf("")
        }
        LaunchedEffect(key1 = city) {
            viewModel.searchState.collectLatest {
                city.value = it.city
            }
        }
        LaunchedEffect(key1 = places) {
            viewModel.searchState.collectLatest {
                places.value = it.placesNames.joinToString(" ")
            }
        }
        SearchTextField(
            text = city.value,
            onValueChange = {
                isHideKeyboard = false
                viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = it))
            },
            onSearch = {
                hideKeyboard()
            },
            iconLeft = {
                IconButton(
                    onClick = {
                        viewModel.onEvent(SearchEvent.OnSearch)
                        hideKeyboard()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            },
            iconRight = {
                IconButton(
                    onClick = {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(id = R.string.current_location)
                    )
                }
            },
            hideKeyboard = isHideKeyboard,
            label = stringResource(id = R.string.city_label),
            onFocusChanged = { cityName ->
                viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = cityName))
            }
        )
        SearchTextField(
            text = places.value,
            onValueChange = {
                isHideKeyboard = false
                viewModel.onEvent(SearchEvent.OnAddPlaceByEditTextField(placeName = it))
            },
            onSearch = {
                hideKeyboard()
            },
            iconLeft = {
                IconButton(
                    onClick = {
                        viewModel.onEvent(SearchEvent.OnSearch)
                        hideKeyboard()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            },
            hideKeyboard = isHideKeyboard,
            label = stringResource(id = R.string.place_label)
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
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(viewModel.placesState.value.places) { place ->
                PlaceItem(place = place, onClick = { })
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
        searchState = viewModel.searchState
    )
}