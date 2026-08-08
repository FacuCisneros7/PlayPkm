package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.SilhouetteImage

@Composable
fun SombrasInfCard(
    pokemon: PokemonApi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(230.dp)
            .height(230.dp)
            .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.circulo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            SilhouetteImage(pokemon.imageUrl!!)
        }
    }
}