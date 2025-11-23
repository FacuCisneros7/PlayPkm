package com.electrofire.playpkm.ui.Components

import android.media.MediaPlayer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Data.Avatar
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


@Composable
fun ChoiseImage(
    statsViewModel: HomeStatsViewModel,
    onImageSelected: (Boolean) -> Unit
) {

    val context = LocalContext.current

    var imageList by remember { mutableStateOf<List<Avatar>>(emptyList()) }
    var selectedImage by remember { mutableStateOf<String?>(null) }
    var searchText by remember { mutableStateOf("") }


    // Traer las imágenes de Firestore
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        val avatars = db.collection("ImagenesDePerfil").get().await()
        imageList = avatars.documents.mapNotNull { doc ->
            val url = doc.getString("url")
            val nombre = doc.getString("nombre")
            if (url != null && nombre != null) Avatar(url, nombre) else null
        }
    }

    val filteredList = imageList.filter {
        it.nombre.contains(searchText, ignoreCase = true)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 14.dp, end = 14.dp)
    ) {
        TextField(
            value = searchText,
            textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
            onValueChange = { searchText = it },
            placeholder = {
                Text(
                    text = "Buscar Pokemon...",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                    textAlign = TextAlign.Center
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                focusedTextColor = MaterialTheme.colorScheme.inverseSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.inverseSurface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.inversePrimary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.inverseSurface,
                cursorColor = MaterialTheme.colorScheme.inverseSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
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
                            color = if (selectedImage == avatar.url) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSecondary,
                            shape = CircleShape
                        )
                        .clickable {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            selectedImage = avatar.url
                            statsViewModel.registrarFoto(avatar.url)

                            onImageSelected(true)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}