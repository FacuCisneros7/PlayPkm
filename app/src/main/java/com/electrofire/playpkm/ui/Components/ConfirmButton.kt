package com.electrofire.playpkm.ui.Components

import android.media.SoundPool
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R

@Composable
fun ConfirmButton(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String = "confirmar"
) {
    val context = LocalContext.current
    val soundPool = remember {
        SoundPool.Builder().setMaxStreams(1).build()
    }
    val soundId = remember {
        soundPool.load(context, R.raw.buttonuisoundeffect, 1)
    }

    Button(
        onClick = {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            onConfirm()
        },
        modifier
            .width(200.dp)
            .height(40.dp)
            .fillMaxSize(),
        elevation = ButtonDefaults.buttonElevation(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (title == "jugar otra vez") {
                Color(0xFF00C853).copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
            },       // Fondo del botón
            contentColor = MaterialTheme.colorScheme.primary, // Color del texto/icono
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.primary
        ),
        enabled = enabled
    ) {
        if (title == "confirmar") {
            Text(
                text = "CONFIRMAR",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "JUGAR DE NUEVO",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}
