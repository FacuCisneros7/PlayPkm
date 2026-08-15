package com.electrofire.playpkm.ui.Components


import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Data.AvatarData
import com.electrofire.playpkm.R

@Composable
fun ChoiseImage(
    selectedImage: String?,
    onImageSelected: (String) -> Unit
) {

    val context = LocalContext.current

    val imageList = remember { AvatarData.avatars }
    var searchText by remember { mutableStateOf("") }

    val filteredList = imageList.filter {
        it.nombre.contains(searchText, ignoreCase = true)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 14.dp, end = 14.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary)
        ) {
            TextField(
                value = searchText,
                textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                onValueChange = { searchText = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                placeholder = {
                    Text(
                        text = "Buscar Pokemon insignia...",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(filteredList) { avatar ->
                Image(
                    painter = rememberAsyncImagePainter(avatar.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (selectedImage == avatar.url) 4.dp else 2.dp,
                            color = if (selectedImage == avatar.url) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .clickable {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            onImageSelected(avatar.url)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}