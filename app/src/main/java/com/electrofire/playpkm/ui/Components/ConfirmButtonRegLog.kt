package com.electrofire.playpkm.ui.Components

import android.media.SoundPool
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.R

@Composable
fun ConfirmButtonRegLog(
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
        modifier = modifier
            .width(305.dp)
            .height(45.dp),
        elevation = ButtonDefaults.buttonElevation(5.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
            contentColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.primary
        ),
        enabled = enabled
    ) {
        val textId = if (title == "iniciar sesion") R.string.log else R.string.reg
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
