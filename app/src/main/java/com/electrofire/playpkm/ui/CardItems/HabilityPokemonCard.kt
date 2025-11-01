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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.ViewModels.HabilityViewModel

@Composable
fun HabilityPokemonCard(modifier: Modifier = Modifier,viewModel: HabilityViewModel = viewModel()) {
    val pokemon = viewModel.pokemon

    if (pokemon == null) {
        Card(
            modifier = modifier.width(230.dp).height(230.dp).fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Loading()
            }
        }
    } else {
        Card(
            modifier = modifier.width(230.dp).height(230.dp).fillMaxSize(),
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
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = null,
                    modifier = modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}