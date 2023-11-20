package com.asabirov.search.presentation.search.screen

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.core_ui.event.UiEvent
import com.asabirov.search.R
import com.asabirov.search.presentation.search.screen.components.CitySearchTextField
import com.asabirov.search.presentation.search.screen.components.PlaceTypesFlowRow
import com.asabirov.search.presentation.search.screen.components.PlacesSearchPagingResult
import com.asabirov.search.presentation.search.screen.components.PlacesSearchTextField
import com.asabirov.search.presentation.search.screen.components.SearchButton
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class
)
@Composable
fun SearchScreen(
    navigateToMap: () -> Unit,
    openPlaceDetails: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isHideKeyboard by remember { mutableStateOf(false) }
    val hideKeyboard = {
        keyboardController?.hide()
        isHideKeyboard = true
    }
    val spacing = LocalSpacing.current
    val snackbarHostState = remember { SnackbarHostState() }
    val locationPermission = rememberPermissionState(
        permission = ACCESS_COARSE_LOCATION
    )
    val places = viewModel.searchPagingFlow?.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    viewModel.searchState
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }

                else -> Unit
            }
        }
    }
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
            CitySearchTextField(viewModel = viewModel)
            PlacesSearchTextField(viewModel = viewModel)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(it)
            ) {
                PlaceTypesFlowRow(keyboardAction = { hideKeyboard() })
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                SearchButton(
                    places = places,
                    hideKeyboardAction = { hideKeyboard() },
                    scope = scope,
                    lazyListState = lazyListState
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                PlacesSearchPagingResult(
                    places = places,
                    lazyListState = lazyListState,
                    viewModel = viewModel,
                    openPlaceDetails = { openPlaceDetails() },
                    navigateToMap = { navigateToMap() },
                    locationPermission = locationPermission
                )
            }
        }
    }
}