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
fun PlaceTypesFlowRow() {
    FlowRow(modifier = Modifier.padding(8.dp)) {
        SetPlacesTypes(placeName = "Restaurants")
        SetPlacesTypes(placeName = "Cafe")
        SetPlacesTypes(placeName = "Museums")
        SetPlacesTypes(placeName = "Cinemas")
        SetPlacesTypes(placeName = "Shopping malls")
        SetPlacesTypes(placeName = "Universities")
        SetPlacesTypes(placeName = "Hospitals")
        SetPlacesTypes(placeName = "Fast food")
        SetPlacesTypes(placeName = "Night Clubs")
        SetPlacesTypes(placeName = "Hookah places")
    }
}

@Composable
private fun SetPlacesTypes(
    placeName: String,
    viewModel: SearchViewModel = hiltViewModel()
) {
    PlaceSelectableButton(
        text = placeName,
        color = MaterialTheme.colorScheme.primary,
        selectedTextColor = Color.White,
        onClick = { isSelected ->
            if (isSelected) viewModel.onEvent(SearchEvent.OnAddPlaceByClickTag(placeName = placeName))
            else viewModel.onEvent(SearchEvent.OnRemovePlace(placeName = placeName))
        },
        viewModel = viewModel
    )
}
