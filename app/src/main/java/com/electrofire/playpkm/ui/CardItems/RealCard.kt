package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.ViewModels.CartaViewModel

@Composable
fun RealCard(modifier: Modifier = Modifier,
             viewModel: CartaViewModel = hiltViewModel()
) {
    val carta = viewModel.carta

    if (carta == null) {
        Card(
            modifier = modifier.width(200.dp).height(280.dp).fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Loading()
            }
        }
    } else {
        AsyncImage(
            model = carta.ImagenReal,
            contentDescription = null,
            modifier = modifier.width(200.dp).height(280.dp).clip(shape = MaterialTheme.shapes.medium),
            contentScale = ContentScale.Fit
        )
    }
}