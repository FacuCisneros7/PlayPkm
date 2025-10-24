package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.electrofire.playpkm.ui.ViewModels.ContadorViewModel

@Composable
fun Contador(modifier: Modifier = Modifier, contadorViewModel : ContadorViewModel = viewModel()){

    val contador = contadorViewModel.contador

    val gradientColors = listOf(
        MaterialTheme.colorScheme.outline,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.tertiary
    )

    LaunchedEffect(Unit) {
        contadorViewModel.iniciarContador()
    }

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
                .padding(6.dp), // grosor del "trazo"
        ) {
            Card(
                modifier.wrapContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                    Text(
                        " $contador ",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp)
                    )
            }
        }
    }
}