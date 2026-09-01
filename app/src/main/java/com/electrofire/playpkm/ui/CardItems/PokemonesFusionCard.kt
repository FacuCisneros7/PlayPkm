package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.Data.Fusion

@Composable
fun PokemonesFusionCard(
    fusionActual: Fusion,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary.copy(0.3f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth().fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (pkmName in fusionActual.Pokemones) {
                Text(
                    pkmName.uppercase(),
                    color = MaterialTheme.colorScheme.primary.copy(0.5f),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
