package com.asabirov.search.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SelectableButton(
    text: String,
    color: Color,
    selectedTextColor: Color,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge
) {
    var isSelected by remember {
        mutableStateOf(false)
    }
    Box(
       contentAlignment = Alignment.Center,
       modifier = modifier
           .padding(4.dp)
           .clip(RoundedCornerShape(80.dp))
           .border(
               width = 2.dp,
               color = color,
               shape = RoundedCornerShape(80.dp)
           )
           .background(
               color = if (isSelected) color else Color.Transparent,
               shape = RoundedCornerShape(80.dp)
           )
           .clickable {
               isSelected = !isSelected
               onClick(isSelected)
           }
           .padding(12.dp)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if(isSelected) selectedTextColor else color,
        )
    }
}