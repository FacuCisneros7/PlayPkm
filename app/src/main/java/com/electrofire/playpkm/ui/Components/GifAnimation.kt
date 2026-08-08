package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.electrofire.playpkm.R

@Composable
fun GifAnimation(
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.gifscr) // tu gif en drawable
                .decoderFactory(ImageDecoderDecoder.Factory())
                .build()
        ),
        contentDescription = "Scroll hint",
        modifier = modifier.size(48.dp)
    )
}
