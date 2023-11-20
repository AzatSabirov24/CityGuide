package com.asabirov.search.presentation.search.screen.components

import android.Manifest
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.core.utils.location.LocationService
import com.asabirov.search.R
import com.asabirov.search.presentation.components.SearchTextField
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@Composable
fun CitySearchTextField(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isHideKeyboard by remember { mutableStateOf(false) }
    val hideKeyboard = {
        keyboardController?.hide()
        isHideKeyboard = true
    }
    val searchState = viewModel.searchState
    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val context = LocalContext.current
    val locationService = LocationService(context)
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
}