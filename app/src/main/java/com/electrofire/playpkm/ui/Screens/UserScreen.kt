package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Domain.openNotificationSettings
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.MusicViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserScreen(
    navController: NavController,
    statsViewModel: HomeStatsViewModel,
    musicViewModel: MusicViewModel
) {
    val user = statsViewModel.userData
    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<String?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val volume by musicViewModel.volume.collectAsState()

    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(selectedImage ?: user.imagen),
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box {
                // Contorno
                Text(
                    text = user.userName!!,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.primary,
                        drawStyle = Stroke(width = 4f)
                    )
                )
                // Relleno
                Text(
                    text = user.userName!!,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box {
                        Text(
                            text = "ESTADÍSTICAS",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSecondary,
                                drawStyle = Stroke(width = 4f)
                            )
                        )
                        Text(
                            text = "ESTADÍSTICAS",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        )
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
                                text = "VICTORIAS",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = user.victorias.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.outline
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
                                color = MaterialTheme.colorScheme.outline
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
                                color = MaterialTheme.colorScheme.outline
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
                                color = MaterialTheme.colorScheme.outline
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
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                                color = if (selectedImage == profileImage) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
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
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(45.dp)
                ) {
                    Text(
                        text = "Cambiar Pokemon insignia",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp)
                    )
                }
            } else {
                Text(
                    text = "Selecciona una insignia para cambiar",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { showSettingsDialog = true },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .width(250.dp)
                    .height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp).padding(end = 6.dp)
                )
                Text(
                    text = "CONFIGURACIÓN",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 13.sp
                    )
                )
            }
        }

        if (showSettingsDialog) {
            Dialog(onDismissRequest = { showSettingsDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box {
                            Text(
                                text = "CONFIGURACIÓN",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 26.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 4f)
                                )
                            )
                            Text(
                                text = "CONFIGURACIÓN",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 26.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }

                        // Volumen
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "VOLUMEN",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Slider(
                                value = volume,
                                onValueChange = { musicViewModel.setVolume(it) },
                                valueRange = 0f..0.2f, // Limitamos a 0.3 para que no sea muy fuerte
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.tertiary,
                                    activeTrackColor = MaterialTheme.colorScheme.tertiary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        // Notificaciones
                        Button(
                            onClick = { openNotificationSettings(context) },
                            modifier = Modifier.fillMaxWidth().height(45.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("ACTIVAR NOTIFICACIONES", style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp))
                        }

                        // Cerrar Sesión
                        Button(
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                showSettingsDialog = false
                                statsViewModel.reset()
                                navController.navigate("register") {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth().height(45.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(2.dp, Color.Red),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red.copy(alpha = 0.1f),
                                contentColor = Color.Red
                            )
                        ) {
                            Text("CERRAR SESIÓN", style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp))
                        }

                        Spacer(Modifier.height(8.dp))

                        // Botón de Cerrar Diálogo
                        Button(
                            onClick = { showSettingsDialog = false },
                            modifier = Modifier.width(150.dp).height(40.dp),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "VOLVER",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}
