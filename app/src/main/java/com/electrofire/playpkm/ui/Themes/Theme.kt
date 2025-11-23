package com.electrofire.playpkm.ui.Themes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val colorScheme = lightColorScheme(
    primary = Blanco,
    onPrimary = CelesteAzulado,

    secondary = AzulOscuro,
    onSecondary = AzulMarino,

    tertiary = CelesteAzuladoDos,

    outline = verdeAgua,
    surface = Blanco,
    onSurface = rojo,

    inversePrimary = grisNuevo,
    inverseSurface = grisOscuro

)

private val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun PLAYPKMTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

