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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
    ExperimentalPermissionsApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SearchScreen(
    navigateToMap: () -> Unit,
    openPlaceDetails: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current
    val snackbarHostState = remember { SnackbarHostState() }
    val locationPermission = rememberPermissionState(
        permission = ACCESS_COARSE_LOCATION
    )
    val places = viewModel.searchPagingFlow
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
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
                PlaceTypesFlowRow()
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                SearchButton(
                    places = places?.collectAsLazyPagingItems(),
                    scope = scope,
                    lazyListState = lazyListState,
                    keyboardHide = { keyboardController?.hide() }
                )
                places?.let {
                    Spacer(modifier = Modifier.height(spacing.spaceSmall))
                    PlacesSearchPagingResult(
                        places = it,
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
}