package com.electrofire.playpkm.ui.Components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GradientBackground(
    colors: List<Color> = listOf(
        Color(0xFF47ACE5),
        Color(0xFF5DD8EC),
        MaterialTheme.colorScheme.primary,
        ),
    durationMillis: Int = 8000
){
    val infiniteTransition= rememberInfiniteTransition()

    val offset = infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
                ),
            repeatMode = RepeatMode.Reverse
            )
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(offset.value,0f),
                end = Offset(0f,offset.value + 1600f)
            )
        )
    )
}