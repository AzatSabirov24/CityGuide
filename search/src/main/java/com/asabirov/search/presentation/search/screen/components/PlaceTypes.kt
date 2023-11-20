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
    keyboardAction: () -> Unit
) {
    FlowRow(modifier = Modifier.padding(8.dp)) {
        SetPlacesTypes(placeName = "Restaurants", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Cafe", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Museums", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Cinemas", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Shopping malls", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Universities", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Hospitals", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Fast food", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Night Clubs", hideKeyboardAction = keyboardAction)
        SetPlacesTypes(placeName = "Hookah places", hideKeyboardAction = keyboardAction)
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
