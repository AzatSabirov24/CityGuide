package com.asabirov.search.presentation.screen.search

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.search.R
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.screen.components.SearchTextField
import com.asabirov.search.presentation.viewmodel.SearchViewModel

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
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
    val searchState = viewModel.searchState

    Column(
        modifier = Modifier.padding()
    ) {
        isLocationPermissionsGranted = locationService.hasLocationPermission()
        SearchTextField(
            text = "kazan",
            onValueChange = {
                isHideKeyboard = false
                viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = it))
            },
            onSearch = {
                hideKeyboard()
                viewModel.onEvent(SearchEvent.OnSearch)
            },
            iconLeftRequired = true,
            iconLeft = {
                IconButton(
                    onClick = {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = viewModel.searchState.city))
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
            when {
                searchState.isSearching -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }

                !searchState.isSearching -> {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(viewModel.placesState.places) { place ->
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
        if (viewModel.placesState.places.isNotEmpty()) {
            Button(
                modifier = Modifier.padding(horizontal = 10.dp),
                onClick = { navigateToMap() }) {
                Text(text = stringResource(id = R.string.on_map))
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