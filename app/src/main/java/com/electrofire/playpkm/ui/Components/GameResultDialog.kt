package com.electrofire.playpkm.ui.Components

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.electrofire.playpkm.R

@Composable
fun GameResultDialog(
    isWin: Boolean = false,
    isGameOver: Boolean = false, // Para juegos infinitos
    puntaje: Int = 0,
    maxScore: Int = 0,
    onHomeClick: () -> Unit,
    onRetryClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Dialog(onDismissRequest = { /* No permitir cerrar al tocar fuera */ }) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.8f, animationSpec = tween(500))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                )
            ) {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                val titleText = when {
                    isGameOver -> stringResource(id = R.string.ninthgame_derrota)
                    isWin -> stringResource(id = R.string.win)
                    else -> stringResource(id = R.string.defeat)
                }

                Box {
                    Text(
                        text = titleText,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    Text(
                        text = titleText,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            color = if (isWin && !isGameOver) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido de la respuesta (Si existe)
                if (content != null) {
                    content()
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Información de Puntos (Solo si es GameOver en juego infinito)
                if (isGameOver) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.ninthgame_puntuation_final),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp)
                        )
                        Text(
                            text = " $puntaje",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.ninthgame_max_puntuacion),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp)
                        )
                        Text(
                            text = " $maxScore",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Botones
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (onRetryClick != null) {
                        Button(
                            onClick = {
                                isVisible = false
                                playSound(context)
                                onRetryClick()
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .height(45.dp),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = if (isGameOver) "JUGAR OTRA VEZ" else "REINTENTAR",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            isVisible = false
                            playSound(context)
                            onHomeClick()
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .height(45.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.ninthgame_home),
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }
}
}

private fun playSound(context: android.content.Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
    mediaPlayer.start()
    mediaPlayer.setOnCompletionListener { it.release() }
}
