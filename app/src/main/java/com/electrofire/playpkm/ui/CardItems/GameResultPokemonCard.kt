package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.PokemonApi

@Composable
fun GameResultPokemonCard(
    pokemon: PokemonApi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.55f)
            .widthIn(max = 200.dp)
            .aspectRatio(1f)
            .padding(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary.copy(0.3f)),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(0.85f),
                contentScale = ContentScale.Fit
            )
        }
    }
}
