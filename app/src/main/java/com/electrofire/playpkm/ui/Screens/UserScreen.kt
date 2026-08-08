package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Domain.openNotificationSettings
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel

@Composable
fun UserScreen(
    statsViewModel: HomeStatsViewModel
) {
    val user = statsViewModel.userData
    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize()) {

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(selectedImage ?: user.imagen),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box {
                // Contorno
                Text(
                    text = user.userName!!,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        drawStyle = Stroke(width = 6f)
                    )
                )
                // Relleno
                Text(
                    text = user.userName!!,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(4.dp, Color.Black),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "ESTADÍSTICAS",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "VICTORIAS",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = user.victorias.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = Color(0xFFF5D828)
                            )
                        }

                        Spacer(modifier = Modifier.width(40.dp))

                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "DERROTAS",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = user.derrotas.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = Color(0xFFF5D828)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "PTS GC",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = user.maxPoints.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = Color(0xFFF5D828)
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "PTS TS",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = user.maxPointsDos.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = Color(0xFFF5D828)
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "PTS BA",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = user.maxPointsTres.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = Color(0xFFF5D828)
                            )
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(user.profileImages) { profileImage ->
                    Image(
                        painter = rememberAsyncImagePainter(profileImage),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (selectedImage == profileImage) 4.dp else 2.dp,
                                color = if (selectedImage == profileImage) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSecondary,
                                shape = CircleShape
                            )
                            .clickable {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                selectedImage = profileImage
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            if (selectedImage != null && selectedImage != user.imagen) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        statsViewModel.registrarFoto(selectedImage!!)
                        selectedImage = null
                    },
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Cambiar Pokemon insignia")
                }
            } else {
                Text(
                    text = "Selecciona una insignia para cambiar",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { openNotificationSettings(context) },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "ACTIVAR NOTIFICACIONES",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}
