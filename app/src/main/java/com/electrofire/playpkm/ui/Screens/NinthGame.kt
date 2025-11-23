package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.BannerAdd
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.NinthViewModel

@Composable
fun NinthGame(
    navController: NavController,
    viewModel: NinthViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel
) {

    val usuario = statsViewModel.userData

    val pokemonA by viewModel.pokemonA.collectAsState()
    val pokemonB by viewModel.pokemonB.collectAsState()
    val puntaje by viewModel.puntaje.collectAsState()
    val estadoJuego by viewModel.estadoJuego.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(estadoJuego) {
        if (estadoJuego == "game_over") {
            statsViewModel.registrarMaxScoreNinthGame(puntaje)
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (pokemonA != null && pokemonB != null) {
                if (estadoJuego == "playing") {

                    Column(
                        verticalArrangement = Arrangement.spacedBy((-12).dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box {
                            Text(
                                text = "GOOD",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "GOOD",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            )
                        }

                        Box {
                            Text(
                                text = "CHOISE",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "CHOISE",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }


                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "SELECCIONA AL POKEMON CON MAYOR STATS BASE!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .width(170.dp)
                            .height(170.dp)
                            .padding(8.dp)
                            .clickable {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                viewModel.elegirPokemon(pokemonA!!)
                            },
                        border = BorderStroke(4.dp, Color(0xFF00C853)),
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
                                model = pokemonA!!.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(140.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "${pokemonA!!.stats.values.sum()}",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(bottom = 8.dp, start = 8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.4f),
                                        shape = MaterialTheme.shapes.small
                                    )
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))


                    Card(
                        modifier = Modifier
                            .width(170.dp)
                            .height(170.dp)
                            .padding(8.dp)
                            .clickable {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                viewModel.elegirPokemon(pokemonB!!)
                            },
                        border = BorderStroke(4.dp, Color(0xFFFF1456)),
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
                                model = pokemonB!!.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(140.dp),
                                contentScale = ContentScale.Fit
                            )

                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "PUNTUACIÓN: ", color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$puntaje", color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                            textAlign = TextAlign.Center
                        )
                    }

                } else {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(100.dp))

                        Box {
                            Text(
                                text = "DERROTA",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 45.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "DERROTA",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 45.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }

                        Spacer(Modifier.height(32.dp))

                        Row {
                            Box {
                                Text(
                                    text = "PUNTUACIÓN FINAL: ",
                                    color = MaterialTheme.colorScheme.inversePrimary,
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "PUNTUACIÓN FINAL: ",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 30.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 2f)
                                    )
                                )
                            }
                            Box {
                                Text(
                                    text = "$puntaje",
                                    color = MaterialTheme.colorScheme.outline,
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "$puntaje",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 30.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 2f)
                                    )
                                )
                            }

                        }

                        Spacer(Modifier.height(32.dp))

                        Row {
                            Box {
                                Text(
                                    text = "MAX PUNTUACIÓN: ",
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "MAX PUNTUACIÓN: ",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 30.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 2f)
                                    )
                                )
                            }
                            Box {
                                Text(
                                    text = "${usuario.maxPoints}",
                                    color = MaterialTheme.colorScheme.outline,
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${usuario.maxPoints}",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 30.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 2f)
                                    )
                                )
                            }

                        }

                        Spacer(Modifier.height(32.dp))

                        ConfirmButton(
                            onConfirm = { viewModel.iniciarJuego() },
                            modifier = Modifier
                                .width(200.dp)
                                .height(50.dp),
                            title = "jugar otra vez"
                        )

                        Spacer(Modifier.height(64.dp))

                        Button(
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                navController.navigate("home") {
                                    popUpTo(Screen.NinthGame.route) { inclusive = true }
                                }
                            },
                            elevation = ButtonDefaults.buttonElevation(5.dp),
                            modifier = Modifier
                                .width(200.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),       // Fondo del botón
                                contentColor = MaterialTheme.colorScheme.primary        // Color del texto/icono
                            )
                        ) {
                            Text(
                                text = "INICIO",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            } else {
                Spacer(Modifier.height(300.dp))
                Loading()
            }
        }
        BannerAdd(Modifier.align(alignment = Alignment.BottomStart))
    }
}