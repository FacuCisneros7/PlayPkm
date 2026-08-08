package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.Movimiento

@Composable
fun MovimientoCard(
    movimiento: Movimiento,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = movimiento.i,
        contentDescription = null,
        modifier = modifier
            .height(190.dp)
            .width(270.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            ),
        contentScale = ContentScale.Crop
    )
}