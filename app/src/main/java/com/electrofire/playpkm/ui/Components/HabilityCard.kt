package com.electrofire.playpkm.ui.Components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.electrofire.playpkm.ui.ViewModels.HabilityViewModel


@Composable
fun HabilityCard(modifier: Modifier = Modifier, viewModel: HabilityViewModel = viewModel()) {
    val pokemonActual = viewModel.pokemon

    if (pokemonActual != null) {

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
                    modifier.wrapContentSize().padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Column(
                        modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (habilidad in pokemonActual.Habilidades) {
                            Text(
                                habilidad.uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp)
                            )
                        }
                    }
                }
            }

        }
    }
}