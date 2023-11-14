package com.asabirov.search.presentation.screen.place_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.presentation.viewmodel.SearchViewModel

@Composable
fun PlaceDetailsScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.placeDetailsState
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = state.place.name ?: "")
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            state.place.photos?.let { photos ->
                items(photos) { photoModel ->
                    PhotoItem(photoUrl = photoModel.url ?: "")
                }
            }
        }
    }
}