package com.asabirov.search.presentation.search.screen.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.viewmodel.SearchViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlaceTypesFlowRow(
    hideKeyboardAction: () -> Unit
) {
    FlowRow(modifier = Modifier.padding(8.dp)) {
        SetPlacesTypes(placeName = "Restaurants", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Cafe", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Museums", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Cinemas", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Shopping malls", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Universities", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Hospitals", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Fast food", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Night Clubs", hideKeyboardAction = hideKeyboardAction)
        SetPlacesTypes(placeName = "Hookah places", hideKeyboardAction = hideKeyboardAction)
    }
}

@Composable
private fun SetPlacesTypes(
    placeName: String,
    viewModel: SearchViewModel = hiltViewModel(),
    hideKeyboardAction: () -> Unit
) {
    PlaceSelectableButton(
        text = placeName,
        color = MaterialTheme.colorScheme.primary,
        selectedTextColor = Color.White,
        onClick = { isSelected ->
            hideKeyboardAction()
            if (isSelected) viewModel.onEvent(SearchEvent.OnAddPlaceByClickTag(placeName = placeName))
            else viewModel.onEvent(SearchEvent.OnRemovePlace(placeName = placeName))
        },
        viewModel = viewModel
    )
}

