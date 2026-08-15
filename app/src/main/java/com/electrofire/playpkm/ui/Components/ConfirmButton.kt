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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
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
    val haptic = LocalHapticFeedback.current
    val soundPool = remember {
        SoundPool.Builder().setMaxStreams(1).build()
    }
    val soundId = remember {
        soundPool.load(context, R.raw.buttonuisoundeffect, 1)
    }

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            onConfirm()
        },
        modifier = modifier
            .width(200.dp)
            .height(40.dp),
        elevation = ButtonDefaults.buttonElevation(3.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.primary
        ),
        enabled = enabled
    ) {
        val textId = when (title) {
            "confirmar" -> R.string.confirm
            "reintentar" -> R.string.retry
            else -> R.string.game_again
        }
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
