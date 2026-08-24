package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun PokemonWithFlag(
    painter: Any?,
    nationality: String?,
    modifier: Modifier = Modifier,
    imageSize: Dp = 130.dp,
    flagSize: Dp = (imageSize.value * 0.25f).dp,
    borderWidth: Dp = 3.dp,
    borderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Box(
        modifier = modifier.size(imageSize),
        contentAlignment = Alignment.Center
    ) {
        // Pokemon Image
        Image(
            painter = if (painter is Int) painterResource(id = painter) else rememberAsyncImagePainter(painter),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = CircleShape
                )
                .padding(borderWidth)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // Flag Overlay
        val flagRes = NationalityUtils.getFlagForNationality(nationality)
        if (flagRes != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp)
                    .size(flagSize)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(id = flagRes),
                    contentDescription = nationality,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
