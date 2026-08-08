package com.electrofire.playpkm.Domain

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ForceUpdateDialog(
    message: String,
    onUpdate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // 🚫 no se puede cerrar
        confirmButton = {
            Button(
                onClick = onUpdate,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,       // Fondo del botón
                    contentColor = MaterialTheme.colorScheme.secondary, // Color del texto/icono
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Actualizar"
                )
            }
        },
        title = { Text("Actualización requerida") },
        text = { Text(message) },
        containerColor = MaterialTheme.colorScheme.onPrimary,
        titleContentColor = MaterialTheme.colorScheme.secondary,
        textContentColor = MaterialTheme.colorScheme.primary
    )
}
