package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.electrofire.playpkm.ui.ViewModels.MovimientoViewModel

@Composable
fun PotenciaCard(modifier: Modifier = Modifier,viewModel: MovimientoViewModel = viewModel()) {
    val movimiento = viewModel.movimiento

    if (movimiento != null) {
        val gradientColors = listOf(
            MaterialTheme.colorScheme.outline,
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.colorScheme.tertiary
        )

        Card(
            modifier.wrapContentSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(10.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Box(
                modifier = modifier
                    .background(
                        brush = Brush.verticalGradient(gradientColors),
                        shape = MaterialTheme.shapes.large,
                    )
                    .padding(2.dp), // grosor del "trazo"
            ) {
                Card(
                    modifier.width(125.dp).height(50.dp).padding(3.dp).fillMaxSize(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            movimiento.Potencia.toString(),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp)
                        )
                    }
                }
            }
        }
    }
}