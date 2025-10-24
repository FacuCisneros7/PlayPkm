package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.electrofire.playpkm.ui.Components.SilhouetteImage
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.ViewModels.DosSiluetasPokemonViewModel

@Composable
fun DosSilhouettePokemonCard(modifier: Modifier = Modifier,viewModel: DosSiluetasPokemonViewModel = hiltViewModel()) {
    val pokemonUno = viewModel.pokemonUno
    val pokemonDos = viewModel.pokemonDos

    val ambosListos = pokemonUno?.Imagen != null && pokemonDos?.Imagen != null

    if (!ambosListos) {
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
                Image(
                    painter = painterResource(id = R.drawable.pokeball),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
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
                    painter = painterResource(id = R.drawable.pokeball),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                    SilhouetteImage(pokemonUno.Imagen)
                    SilhouetteImage(pokemonDos.Imagen)
            }
        }
    }
}