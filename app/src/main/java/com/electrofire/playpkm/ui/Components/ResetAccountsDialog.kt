package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ResetAccountsDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "AVISO",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    Text(
                        text = "AVISO",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }

                Text(
                    text = "Se han reseteado las cuentas anteriores a la versión actual.\n\nDebes registrarte nuevamente si es la primera vez que juegas esta versión.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(150.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "ENTENDIDO",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
