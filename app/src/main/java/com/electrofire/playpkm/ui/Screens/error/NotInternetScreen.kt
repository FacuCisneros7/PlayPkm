package com.electrofire.playpkm.ui.Screens.error

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.electrofire.playpkm.R

@Composable
fun NotInternetScreen(onRetry: (() -> Unit)? = null) {
    Dialog(onDismissRequest = { /* Bloqueado para asegurar reintento */ }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(4.dp, Color.Red.copy(alpha = 0.7f)),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título con Stroke Rojo
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "SIN CONEXIÓN",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 28.sp,
                            color = Color.Red,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    Text(
                        text = "SIN CONEXIÓN",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 28.sp,
                            color = Color.White
                        )
                    )
                }

                // Imagen de Rotom
                Image(
                    painter = painterResource(id = R.drawable.rotom),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )

                Text(
                    text = "Asegúrese de tener una conexión a internet estable para continuar jugando PlayPkm.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                // Botón Reintentar (Solo aparece si se le pasa una acción)
                if (onRetry != null) {
                    Button(
                        onClick = { onRetry.invoke() },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, Color.Red),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.2f),
                            contentColor = Color.Red
                        )
                    ) {
                        Text(
                            text = "REINTENTAR",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // Si no hay acción, mostramos un mensaje sutil de espera
                    Text(
                        text = "Esperando conexión...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
