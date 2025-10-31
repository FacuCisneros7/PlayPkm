package com.electrofire.playpkm.ui.CardItems

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.R

@Composable
fun PokemonApiCard(modifier: Modifier = Modifier,
                   pokemon : PokemonApi,
                   onSelected: Boolean,
                   onClick: () -> Unit
) {

    val context = LocalContext.current

        Card(
            modifier = modifier
                .width(160.dp)
                .height(160.dp)
                .padding(8.dp)
                .clickable {
                    val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener { it.release() }

                    onClick()
                           },
            border = if (onSelected) { BorderStroke(4.dp, Color(0xFF00C853))
            } else {
                BorderStroke(4.dp, MaterialTheme.colorScheme.secondary)
            },
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = null,
                    modifier = modifier
                        .width(130.dp)
                        .height(130.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
}