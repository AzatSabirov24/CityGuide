package com.asabirov.search.presentation.search.screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.R
import com.asabirov.search.presentation.components.SearchTextField
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlacesSearchTextField(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState = viewModel.searchState
    val keyboardController = LocalSoftwareKeyboardController.current
    var isHideKeyboard by remember { mutableStateOf(false) }
    val hideKeyboard = {
        keyboardController?.hide()
        isHideKeyboard = true
    }
    SearchTextField(
        text = searchState.placesNames.joinToString(" "),
        onValueChange = { placeName ->
            isHideKeyboard = false
            viewModel.onEvent(SearchEvent.OnAddPlaceByEditTextField(placeName = placeName))
        },
        onSearch = {
            hideKeyboard()
            viewModel.onEvent(SearchEvent.OnSearch)
        },
        iconRightRequired = true,
        iconRight = {
            IconButton(
                onClick = {
                    viewModel.onEvent(SearchEvent.OnRemoveAllPlaces)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(id = R.string.close)
                )
            }
        },
        label = stringResource(id = R.string.place_label),
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}