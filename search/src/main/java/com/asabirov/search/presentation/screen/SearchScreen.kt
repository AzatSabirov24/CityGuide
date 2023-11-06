package com.asabirov.search.presentation.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.core.utils.location.LocationService
import com.asabirov.search.R
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.place.Place
import com.asabirov.search.presentation.screen.components.SearchTextField
import com.asabirov.search.presentation.screen.components.SelectableButton
import com.asabirov.search.presentation.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val locationService = LocationService(context)
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    locationService.getCurrentCity(locationService.hasLocationPermission()) {
                        viewModel.onEvent(SearchEvent.OnChangeCityName(it ?: ""))
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
        val text = remember {
            mutableStateOf("")
        }
        SearchTextField(
            text = text,
            onValueChange = { text.value = it },
            onSearch = { },
            iconSearch = {
                IconButton(
                    onClick = {
                        viewModel.onEvent(SearchEvent.OnSearch)
                        println("qqq ${state}")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            },
            iconLocation = {
                IconButton(
                    onClick = {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        text.value = viewModel.state.cityName
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(id = R.string.current_location)
                    )
                    text.value = viewModel.state.cityName
                    viewModel.onEvent(SearchEvent.OnAddQuery(query = "+${viewModel.state.cityName}"))
                }
            }
        )

        Row {
            SelectableButton(
                text = "Museums",
                color = MaterialTheme.colorScheme.primary,
                selectedTextColor = Color.White,
                onClick = {
                    viewModel.onSelectPlace(Place.Museum)
                    viewModel.onEvent(SearchEvent.OnAddQuery("+museums"))
                },
                textStyle = MaterialTheme.typography.labelMedium
            )
        }
    }
}