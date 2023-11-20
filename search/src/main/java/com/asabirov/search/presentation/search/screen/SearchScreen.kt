package com.asabirov.search.presentation.search.screen

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.core_ui.event.UiEvent
import com.asabirov.search.R
import com.asabirov.search.presentation.components.SearchScaffold
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
    ExperimentalComposeUiApi::class,
    ExperimentalPermissionsApi::class
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
    SearchScaffold(
        actionName = stringResource(
            id = R.string.go_to_settings
        )
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            CitySearchTextField(viewModel = viewModel)
            PlacesSearchTextField(viewModel = viewModel)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
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