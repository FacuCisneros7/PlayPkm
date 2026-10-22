package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.electrofire.playpkm.Data.Carta
import com.electrofire.playpkm.ui.Components.Loading

@Composable
fun RealCard(
    carta: Carta,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(carta.ImagenReal)
            .crossfade(true)
            .listener(
                onError = { _, result ->
                    android.util.Log.e("IMAGE_ERROR", "Error cargando ImagenReal: ${carta.Nombre}")
                    android.util.Log.e("IMAGE_ERROR", "URL: ${carta.ImagenReal}")
                    android.util.Log.e("IMAGE_ERROR", "Motivo: ${result.throwable.message}")
                }
            )
            .build(),
        contentDescription = null,
        modifier = modifier
            .width(200.dp)
            .height(280.dp)
            .clip(shape = MaterialTheme.shapes.medium),
        contentScale = ContentScale.Fit,
        loading = {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Loading()
            }
        },
        error = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Loading()
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Error al cargar solución",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp
                )
            }
        }
    )
}
