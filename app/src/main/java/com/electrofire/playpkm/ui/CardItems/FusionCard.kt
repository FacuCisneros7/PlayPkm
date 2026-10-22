package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.electrofire.playpkm.Data.Fusion
import com.electrofire.playpkm.ui.Components.Loading

@Composable
fun FusionCard(
    fusion: Fusion,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.55f)
            .widthIn(max = 200.dp)
            .aspectRatio(1f)
            .padding(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary.copy(0.3f)),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(fusion.Imagen)
                    .crossfade(true)
                    .listener(
                        onError = { _, result ->
                            android.util.Log.e("IMAGE_ERROR", "Error cargando fusión ID: ${fusion.id}")
                            android.util.Log.e("IMAGE_ERROR", "URL: ${fusion.Imagen}")
                            android.util.Log.e("IMAGE_ERROR", "Motivo: ${result.throwable.message}")
                        }
                    )
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(0.85f),
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
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Asegúrese de tener una conexión estable",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 10.sp,
                            fontSize = 8.sp
                        )
                    }
                }
            )
        }
    }
}
