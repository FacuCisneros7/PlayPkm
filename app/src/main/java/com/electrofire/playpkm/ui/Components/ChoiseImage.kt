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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


@Composable
fun ChoiseImage(statsViewModel: HomeStatsViewModel) {

    val context = LocalContext.current

    var imageList by remember { mutableStateOf<List<String>>(emptyList()) }

    var selectedImage by remember { mutableStateOf<String?>(null) }

    // Traer las imágenes de Firestore
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        val avatars = db.collection("ImagenesDePerfil").get().await()
        imageList = avatars.documents.mapNotNull { it.getString("url") }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(imageList) { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (selectedImage == imageUrl) 3.dp else 1.dp,
                            color = if (selectedImage == imageUrl) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .clickable {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            selectedImage = imageUrl
                            statsViewModel.registrarFoto(imageUrl)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}