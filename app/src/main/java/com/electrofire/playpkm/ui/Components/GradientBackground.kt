package com.electrofire.playpkm.ui.Components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.secondary,
    ),
    durationMillis: Int = 8000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val offsetFactor = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val animatedOffset = -200f + (offsetFactor.value * 700f)
                drawRect(
                    brush = Brush.linearGradient(
                        colors = colors,
                        start = Offset(animatedOffset, 0f),
                        end = Offset(0f, animatedOffset + size.height + 200f)
                    )
                )
            }
    )
}
