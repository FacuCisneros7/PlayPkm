package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.electrofire.playpkm.ui.ViewModels.MovimientoViewModel

@Composable
fun MovimientoCard(
    modifier: Modifier = Modifier,
    viewModel: MovimientoViewModel = hiltViewModel()
) {

    val movimiento = viewModel.movimiento

    if (movimiento == null) {
        Card(
            modifier = Modifier.size(200.dp),
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
            model = movimiento.i,
            contentDescription = null,
            modifier = Modifier
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
}