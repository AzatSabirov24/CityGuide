package com.asabirov.search.presentation.place_details.screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.core.utils.phone_dialer.PhoneDialer
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.core_ui.event.UiEvent
import com.asabirov.search.R
import com.asabirov.search.presentation.components.DropDown
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun PlaceDetailsScreen(
    navigateToMap: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.placeDetailsState
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val phoneDialer = PhoneDialer(context)
    val snackbarHostState = remember { SnackbarHostState() }
    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val lazyListState = rememberLazyListState()
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
                                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
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
            modifier = Modifier
                .padding(it)
        ) {
            Column {
                Text(
                    text = state.place.name ?: "",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(spacing.spaceSmall)
                )
                LazyRow(
                    modifier = Modifier
                        .padding(vertical = spacing.spaceSmall),
                    state = lazyListState,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
                ) {
                    state.place.photos?.let { photos ->
                        items(photos) { photoModel ->
                            PhotoItem(photoUrl = photoModel.url ?: "")
                        }
                    }
                }
            }
            LazyColumn {
                item {
                    Text(
                        text = state.place.address ?: "",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = spacing.spaceSmall)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = state.place.phoneNumber ?: "",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(
                                    top = spacing.spaceSmall,
                                    start = spacing.spaceMedium
                                )
                                .clickable {
                                    phoneDialer(state.place.phoneNumber ?: "")
                                }
                        )
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                if (locationPermission.status.isGranted) {
                                    navigateToMap()
                                } else {
                                    viewModel.showSnackBar()
                                }
                            }
                        ) {
                            Text(text = stringResource(id = R.string.on_map))
                        }
                    }

                    if (state.place.openingTime?.isNotEmpty() == true) {
                        DropDown(
                            text = stringResource(id = R.string.work_time),
                            modifier = Modifier.padding(
                                top = spacing.spaceSmall,
                                start = spacing.spaceSmall,
                                bottom = spacing.spaceSmall
                            )
                        ) {
                            Text(
                                text = viewModel.placeDetailsState.place.openingTime?.joinToString { it }
                                    ?.replace(", ", "\n") ?: "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                            )
                        }
                    }
                }
            }
        }
    }
}