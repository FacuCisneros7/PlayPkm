package com.electrofire.playpkm.ui.Components

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Data.AvatarItem
import com.electrofire.playpkm.R

object AvatarList {
    val items = listOf(
        AvatarItem("0001", "bulbasaur"),
        AvatarItem("0004", "charmander"),
        AvatarItem("0007", "squirtle"),
        AvatarItem("0025", "pikachu"),
        AvatarItem("0039", "jigglypuff"),
        AvatarItem("0052", "meowth"),
        AvatarItem("0133", "eevee"),
        AvatarItem("0447", "riolu")
    )
}

@Composable
fun ChoiseImage(
    selectedAvatarId: String,
    onAvatarSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val imageList = remember { AvatarList.items }

    // 2 filas de 4 elementos cada una
    val rows = imageList.chunked(4)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 14.dp)
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowItems.forEach { avatar ->
                    Image(
                        painter = rememberAsyncImagePainter("file:///android_asset/portraits/${avatar.id}.png"),
                        contentDescription = avatar.name,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (selectedAvatarId == avatar.id) 4.dp else 2.dp,
                                color = if (selectedAvatarId == avatar.id) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.4f
                                ),
                                shape = CircleShape
                            )
                            .clickable {
                                val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                onAvatarSelected(avatar.id)
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
