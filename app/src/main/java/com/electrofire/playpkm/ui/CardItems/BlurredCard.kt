package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.Carta

@Composable
fun BlurredCard(
    carta: Carta,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = carta.ImagenBorrosa,
        contentDescription = null,
        modifier = modifier
            .width(181.dp)
            .height(254.dp)
            .clip(shape = MaterialTheme.shapes.medium),
        contentScale = ContentScale.Fit
    )
}