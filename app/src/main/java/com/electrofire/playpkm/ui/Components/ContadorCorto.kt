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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContadorCorto(modifier: Modifier = Modifier, contador: Int) {


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
                .padding(6.dp),
        ) {
            Card(
                modifier.wrapContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    " $contador ",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp)
                )
            }
        }
    }
}