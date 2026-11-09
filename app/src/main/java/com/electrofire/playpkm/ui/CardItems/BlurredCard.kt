package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.Data.Carta

@Composable
fun BlurredCard(
    carta: Carta,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = carta.ImagenReal),
        contentDescription = null,
        modifier = modifier
            .width(181.dp)
            .height(254.dp)
            .blur(radius = 12.dp)
            .clip(shape = MaterialTheme.shapes.medium),
        contentScale = ContentScale.Fit
    )
}
