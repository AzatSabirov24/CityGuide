package com.asabirov.search.presentation.search.screen.components

import android.Manifest
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.core.utils.location.LocationService
import com.asabirov.search.R
import com.asabirov.search.presentation.components.SearchTextField
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CitySearchTextField(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState = viewModel.searchState
    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val context = LocalContext.current
    val locationService = LocationService(context)
    SearchTextField(
        text = "Казань",
        onValueChange = { cityName ->
            viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = cityName))
        },
        onSearch = { viewModel.onEvent(SearchEvent.OnSearch) },
        label = stringResource(id = R.string.city_label),
        onFocusChanged = { cityName ->
            viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = cityName))
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}