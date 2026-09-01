package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.Data.Movimiento

@Composable
fun PotenciaCard(
    movimiento: Movimiento,
) {

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        Row {
            Text(
                text = "Potencia: ${movimiento.p}",
                color = MaterialTheme.colorScheme.primary.copy(0.5f),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }


}