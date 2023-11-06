package com.asabirov.search.presentation.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import com.asabirov.search.presentation.screen.components.SearchTextField
import com.asabirov.search.presentation.screen.components.SelectableButton
import com.asabirov.search.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val locationService = LocationService(context)
    val keyboardController = LocalSoftwareKeyboardController.current
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
        val city = remember {
            mutableStateOf("")
        }
        SearchTextField(
            text = city,
            onValueChange = {
                city.value = it
            },
            onSearch = {
                keyboardController?.hide()
                viewModel.onEvent(SearchEvent.OnSearch)
                viewModel.onEvent(SearchEvent.OnChangeCityName(city.value))
            },
            iconSearch = {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.onEvent(SearchEvent.OnChangeCityName(city.value))
                        viewModel.onEvent(SearchEvent.OnSearch)
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
                        city.value = state.cityName
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(id = R.string.current_location)
                    )
                    city.value = state.cityName
                    viewModel.onEvent(SearchEvent.OnAddQuery(query = "+${state.cityName}"))
                }
            }
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            SetPlace(name = "Restaurants", city = city, query = "+restaurant")
            SetPlace(name = "Museums", city = city, query = "+museums")
            SetPlace(name = "Cinemas", city = city, query = "+Cinemas")
            SetPlace(name = "Shopping malls", city = city, query = "+Shopping_malls")
            SetPlace(name = "Universities", city = city, query = "+Universities")
            SetPlace(name = "Restaurants", city = city, query = "+restaurant")
            SetPlace(name = "Night Clubs", city = city, query = "+Night_Clubs")
        }
    }
}

@Composable
private fun SetPlace(
    name: String,
    city: State<String>,
    query: String,
    viewModel: SearchViewModel = hiltViewModel()
) {
    SelectableButton(
        text = name,
        color = MaterialTheme.colorScheme.primary,
        selectedTextColor = Color.White,
        onClick = { isSelected ->
            viewModel.onEvent(SearchEvent.OnChangeCityName(city.value))
            if (isSelected) viewModel.onEvent(SearchEvent.OnAddQuery(query = query))
            else viewModel.onEvent(SearchEvent.OnRemoveQuery(query = query))
        }
    )
}