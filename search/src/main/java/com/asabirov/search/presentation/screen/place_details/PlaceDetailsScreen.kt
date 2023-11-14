package com.asabirov.search.presentation.screen.place_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.core.utils.phone_dialer.PhoneDialer
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.search.R
import com.asabirov.search.presentation.screen.components.DropDown
import com.asabirov.search.presentation.viewmodel.SearchViewModel

@Composable
fun PlaceDetailsScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.placeDetailsState
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val phoneDialer = PhoneDialer(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = state.place.name ?: "",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(spacing.spaceSmall)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.spaceSmall)
            ) {
                state.place.photos?.let { photos ->
                    items(photos) { photoModel ->
                        PhotoItem(photoUrl = photoModel.url ?: "")
                    }
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = state.place.address ?: "",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(horizontal = spacing.spaceSmall)
                )
                Text(
                    text = state.place.phoneNumber ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(
                            top = spacing.spaceSmall,
                            start = spacing.spaceSmall
                        )
                        .clickable {
                            phoneDialer(state.place.phoneNumber ?: "")
                        }
                )
                if (state.place.openingTime?.isNotEmpty() == true) {
                    DropDown(
                        text = stringResource(id = R.string.work_time),
                        modifier = Modifier.padding(
                            top = spacing.spaceSmall,
                            start = spacing.spaceSmall
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