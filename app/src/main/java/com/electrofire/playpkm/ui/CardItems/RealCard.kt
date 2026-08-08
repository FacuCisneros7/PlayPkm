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
fun RealCard(
    carta: Carta,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = carta.ImagenReal,
        contentDescription = null,
        modifier = modifier
            .width(200.dp)
            .height(280.dp)
            .clip(shape = MaterialTheme.shapes.medium),
        contentScale = ContentScale.Fit
    )
}