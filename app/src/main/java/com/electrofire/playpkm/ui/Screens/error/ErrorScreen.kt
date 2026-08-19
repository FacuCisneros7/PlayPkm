package com.electrofire.playpkm.ui.Screens.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground

@Composable
fun ErrorScreen(onRetry: (() -> Unit)? = null, showBackground: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showBackground) {
            GradientBackground(
                listOf(
                    Color(0xFFE91E63), // Un tono rosado/fucsia para diferenciarlo del rojo de internet
                    MaterialTheme.colorScheme.primary,
                    Color(0xFFE91E63),
                )
            )
        }

        Column(
            Modifier
                .wrapContentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    text = "ERROR INESPERADO",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary,
                        drawStyle = Stroke(width = 4f)
                    )
                )
                Text(
                    text = "ERROR INESPERADO",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.rotom),
                contentDescription = null,
                modifier = Modifier
                    .height(300.dp)
                    .wrapContentWidth()
            )

            Spacer(modifier = Modifier.height(22.dp))

            Box {
                Text(
                    text = "Ha ocurrido un error inesperado, por favor intenta mas tarde",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.primary,
                        drawStyle = Stroke(width = 2f)
                    )
                )
                Text(
                    text = "Ha ocurrido un error inesperado, por favor intenta mas tarde",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(32.dp))
                ConfirmButton(
                    onConfirm = onRetry,
                    title = "reintentar"
                )
            }
        }

    }


}