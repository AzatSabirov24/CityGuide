package com.asabirov.search.presentation.screen.place_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
    Box(modifier = Modifier.fillMaxSize()){
        Text(text = state.place?.name ?: "")
    }
}