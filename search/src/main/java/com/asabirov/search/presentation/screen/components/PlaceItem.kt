package com.asabirov.search.presentation.screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.search.domain.model.search_by_text.Place

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier,
    place: Place,
    onClick: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = place.name)
    }
}