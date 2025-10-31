package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.R

@Composable
fun Hearts(
    total: Int = 3,
    actuales: Int,
    modifier: Modifier = Modifier,
    iconoLleno: Painter = painterResource(id = R.drawable.corazon),
    iconoVacio: Painter = painterResource(id = R.drawable.corazonnegro)
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    for (i in 1..total) {
                        Card(modifier
                            .size(74.dp)
                            .clip(CircleShape), // 🔹 Hace que la sombra también sea circular
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(15.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Image(
                                painter = if (i <= actuales) iconoLleno else iconoVacio,
                                contentDescription = null,
                                modifier = Modifier.size(74.dp)
                            )
                        }

                    }
                }
            }
}