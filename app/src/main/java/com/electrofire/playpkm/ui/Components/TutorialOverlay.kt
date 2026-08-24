package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TutorialOverlay(
    show: Boolean,
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    
    val steps = listOf(
        TutorialStep(
            text = "¡Bienvenido! Aquí puedes consultar las reglas e información de los juegos.",
            isToolbar = true
        ),
        TutorialStep(
            text = "Desliza hacia abajo para ver más minijuegos y desafíos.",
            isToolbar = false
        )
    )

    if (show) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.99f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (currentStep < steps.size - 1) {
                        currentStep++
                    } else {
                        onComplete()
                    }
                }
        ) {
            val step = steps[currentStep]

            // Background with hole
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(Color.Black.copy(alpha = 0.75f))
                
                val holeRect = if (step.isToolbar) {
                    // Botón de info (Top Left aprox) - Ajustado más arriba
                    Rect(Offset(10.dp.toPx(), 42.dp.toPx()), Size(45.dp.toPx(), 45.dp.toPx()))
                } else {
                    // Lista de juegos (Centro)
                    Rect(Offset(20.dp.toPx(), 280.dp.toPx()), Size(size.width - 40.dp.toPx(), 320.dp.toPx()))
                }

                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = holeRect.topLeft,
                    size = holeRect.size,
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                    blendMode = BlendMode.Clear
                )
            }

            // Text Card Positioning
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = if (step.isToolbar) Alignment.TopStart else Alignment.Center
            ) {
                Column(
                    horizontalAlignment = if (step.isToolbar) Alignment.Start else Alignment.CenterHorizontally,
                    modifier = Modifier.offset(
                        x = if (step.isToolbar) 5.dp else 0.dp,
                        y = if (step.isToolbar) 95.dp else 0.dp
                    )
                ) {
                    if (step.isToolbar) {
                        ArrowUp()
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                        elevation = CardDefaults.cardElevation(6.dp),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(if (step.isToolbar) 0.75f else 0.85f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = if (step.isToolbar) Alignment.Start else Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = step.text,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = if (step.isToolbar) TextAlign.Start else TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (currentStep < steps.size - 1) "TOCA PARA SEGUIR" else "¡LISTO!",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    
                    if (!step.isToolbar) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ArrowDown()
                    }
                }
            }
        }
    }
}

@Composable
fun ArrowUp() {
    Canvas(
        modifier = Modifier
            .size(25.dp)
            .offset(x = 12.dp)
            .rotate(-20f)
    ) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, Color.White)
    }
}

@Composable
fun ArrowDown() {
    Canvas(modifier = Modifier.size(25.dp)) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width / 2, size.height)
            close()
        }
        drawPath(path, Color.White)
    }
}

data class TutorialStep(
    val text: String,
    val isToolbar: Boolean
)
