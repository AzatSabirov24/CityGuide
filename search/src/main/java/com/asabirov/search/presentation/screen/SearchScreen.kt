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
import com.asabirov.search.presentation.screen.components.SearchTextField
import com.asabirov.search.presentation.screen.components.SelectableButton
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
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
//                        viewModel.updateCityQuery(cityName = it ?: "")
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
        val place = remember {
            mutableStateOf("")
        }
        LaunchedEffect(key1 = city) {
            viewModel.state.collectLatest {
                city.value = it.city
            }
        }
        SearchTextField(
            text = city,
            onValueChange = {
                isHideKeyboard = false
                viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = it))
            },
            onSearch = {
                hideKeyboard()
//                println("qqq ->onSearch->${state.cityName}")
            },
            iconRight = {
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
            iconLeft = {
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
            onFocusChanged = { _, cityName ->
                viewModel.onEvent(SearchEvent.OnChangeCityName(cityName = cityName))
                println("qqq ->SearchScreen->${viewModel.state.value.city}")
            }
        )
        SearchTextField(
            text = city,
            onValueChange = {
                isHideKeyboard = false
            },
            onSearch = {
                hideKeyboard()
            },
            iconRight = {
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
            iconLeft = {
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
            onFocusChanged = { _, cityName ->

            }
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            SetPlace(name = "Restaurants", query = "+restaurant", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Museums", query = "+museums", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Cinemas", query = "+Cinemas", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Shopping malls", query = "+Shopping_malls", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Universities", query = "+Universities", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Hospitals", query = "+Hospitals", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Fast food", query = "+fast_food", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Night Clubs", query = "+Night_Clubs", onClick = {
                hideKeyboard()
            })
            SetPlace(name = "Hookah places", query = "+hookah", onClick = {
                hideKeyboard()
            })
        }
    }
}

@Composable
private fun SetPlace(
    name: String,
    query: String,
    viewModel: SearchViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    SelectableButton(
        text = name,
        color = MaterialTheme.colorScheme.primary,
        selectedTextColor = Color.White,
        onClick = { isSelected ->
            onClick()
//            viewModel.onEvent(SearchEvent.OnChangeCityName(city.value))
//            if (isSelected) viewModel.onEvent(SearchEvent.OnAddQuery(query = query))
//            else viewModel.onEvent(SearchEvent.OnRemoveQuery(query = query))
        }
    )
}