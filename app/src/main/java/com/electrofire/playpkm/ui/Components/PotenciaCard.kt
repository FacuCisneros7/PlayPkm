package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.Data.Movimiento

@Composable
fun PotenciaCard(
    movimiento: Movimiento,
    modifier: Modifier = Modifier
) {
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
                modifier
                    .wrapContentSize()
                    .padding(3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Row {
                    Box {
                        Text(
                            text = "Potencia: ",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.primary,
                                drawStyle = Stroke(width = 2f)
                            ),
                        )
                        Text(
                            text = "Potencia: ",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.secondary
                            ),
                        )
                    }
                    Text(
                        text = movimiento.p.toString(),
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}