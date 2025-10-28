package com.electrofire.playpkm.ui.Components

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R

@Composable
fun LoserCard(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
){
    val context = LocalContext.current

    Card(
        modifier = modifier.width(220.dp).height(155.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface),
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.secondary)
    ) {

        Column (
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "DERROTA",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener { it.release() }

                    onButtonClick()
                          },
                modifier = Modifier.width(125.dp).height(40.dp).fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,       // Fondo del botón
                    contentColor = MaterialTheme.colorScheme.onSurface        // Color del texto/icono
                )
            ) {
                Text(
                    text = "Inicio",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }

}
