package com.asabirov.search.presentation.screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.search.domain.model.search_by_text.PlaceModel

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier,
    place: PlaceModel,
    onClick: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    Box(modifier = modifier
        .width(200.dp)
        .height(200.dp)
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            Text(text = place.name)
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = place.photoUrl)
                        .apply(block = @SuppressLint("ResourceType")
                        fun ImageRequest.Builder.() {
                            crossfade(true)
//                            error(R.drawable.ic_burger)
//                            fallback(R.drawable.ic_burger)
                        }).build()
                ),
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        }
    }
}