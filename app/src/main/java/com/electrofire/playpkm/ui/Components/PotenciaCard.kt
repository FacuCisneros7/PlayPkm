package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.Data.Movimiento

@Composable
fun PotenciaCard(
    movimiento: Movimiento,
    modifier: Modifier = Modifier
) {

    Row {
        Box {
            Text(
                text = "Potencia: ",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    drawStyle = Stroke(width = 3f)
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