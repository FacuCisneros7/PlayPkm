package com.electrofire.playpkm.ui.Components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel


@Composable
fun ChoiseImage(statsViewModel: HomeStatsViewModel){

    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(imagenUri != null){
            Image(painter = rememberAsyncImagePainter(imagenUri),
                contentDescription = "",
                modifier = Modifier.size(120.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        else{
            Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.Gray)
                .clickable{launcher.launch("image/*")},
                contentAlignment = Alignment.Center
                ) {
                Text("Seleccionar foto")
            }
        }
    }

    LaunchedEffect(imagenUri) {
        imagenUri?.let { statsViewModel.registrarFoto(it.toString()) }
    }}