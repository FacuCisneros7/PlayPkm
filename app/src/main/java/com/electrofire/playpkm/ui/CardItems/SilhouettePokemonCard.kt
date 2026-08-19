package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.ui.Components.SilhouetteImage

@Composable
fun SilhouettePokemonCard(
    pokemon: PokemonApi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(230.dp)
            .height(230.dp)
            .padding(8.dp),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SilhouetteImage(pokemon.imageUrl ?: "")
        }
    }
}
