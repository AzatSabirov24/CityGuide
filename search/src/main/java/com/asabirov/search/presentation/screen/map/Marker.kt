package com.asabirov.search.presentation.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Marker() {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(85.dp)
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .rotate(45f)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp))
                .border(
                    2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF065C74),
                            Color(0xFF06C7B2)
                        )
                    )
                )
        )
    }
}