package com.asabirov.search.presentation.screen.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.asabirov.core.extensions.isNotNullAndFalse
import com.asabirov.core.extensions.isNotNullAndTrue
import com.asabirov.core_ui.LocalSpacing
import com.asabirov.search.R
import com.asabirov.search.domain.model.places.PlaceModel

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier,
    place: PlaceModel,
    onClick: (String) -> Unit,
    color: Color = Color.Gray
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(40f))
            .clickable { onClick(place.id) }
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(40f)
            )
            .width(300.dp)
            .height(250.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = place.photoUrl)
                    .apply(block = @SuppressLint("ResourceType")
                    fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            ),
            contentDescription = place.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.name,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(
                        start = spacing.spaceSmall,
                        top = spacing.spaceExtraSmall
                    )
                    .weight(1f)
            )
            Text(
                text = when {
                    place.isOpenNow.isNotNullAndTrue() -> stringResource(id = R.string.open)
                    place.isOpenNow.isNotNullAndFalse() -> stringResource(id = R.string.close)
                    else -> ""
                },
                color = if (place.isOpenNow == true) Color.Green else Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    end = spacing.spaceMedium,
                    top = spacing.spaceExtraSmall,
                    start = spacing.spaceSmall
                )
            )
        }
//        place.
        place.rating?.let {
            Text(
                text = it.toString(),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(
                        start = spacing.spaceSmall,
                        bottom = spacing.spaceSmall
                    )
            )
        }
    }
}