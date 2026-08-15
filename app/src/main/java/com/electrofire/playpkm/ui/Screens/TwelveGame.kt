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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.TwelveViewModel
import com.electrofire.playpkm.ui.ViewModels.UIState

@Composable
fun TwelveGame(
    navController: NavController,
    viewModel: TwelveViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel
) {

    val usuario = statsViewModel.userData
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(state) {
        if (state is UIState.Success && (state as UIState.Success).data.isGameOver) {
            statsViewModel.registrarMaxScoreTwelveGame((state as UIState.Success).data.puntaje)
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {

        when (val currentState = state) {
            is UIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Loading()
                }
            }

            is UIState.Error -> {
                if (currentState.message.contains("conexión", ignoreCase = true)) {
                    NotInternetScreen(onRetry = { viewModel.iniciarJuego() })
                } else {
                    ErrorScreen(onRetry = { viewModel.iniciarJuego() })
                }
            }

            is UIState.Success -> {
                val gameData = currentState.data
                val pokemonA = gameData.pokemonA
                val pokemonB = gameData.pokemonB
                val puntaje = gameData.puntaje
                val isGameOver = gameData.isGameOver

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!isGameOver) {

                        Column(
                            verticalArrangement = Arrangement.spacedBy((-12).dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box {
                                Text(
                                    text = "BEFORE",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 40.sp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        drawStyle = Stroke(width = 4f)
                                    )
                                )
                                Text(
                                    text = "BEFORE",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 40.sp,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                )
                            }

                            Box {
                                Text(
                                    text = "OR AFTER",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 40.sp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        drawStyle = Stroke(width = 4f)
                                    )
                                )
                                Text(
                                    text = "OR AFTER",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 40.sp,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                )
                            }
                        }


                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = stringResource(id = R.string.twelvegame_description),
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
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }

                                    viewModel.elegirPokemon(pokemonA)
                                },
                            border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                AsyncImage(
                                    model = pokemonA.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(140.dp)
                                        .height(140.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Text(
                                    text = "NroDex: ${pokemonA.id}",
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
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
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }

                                    viewModel.elegirPokemon(pokemonB)
                                },
                            border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                AsyncImage(
                                    model = pokemonB.imageUrl,
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
                                text = stringResource(id = R.string.ninthgame_puntuation),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = " $puntaje", color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                textAlign = TextAlign.Center
                            )
                        }

                    } else {
                        GameResultDialog(
                            isGameOver = true,
                            puntaje = puntaje,
                            maxScore = usuario.maxPointsTres,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.TwelveGame.route) { inclusive = true }
                                }
                            },
                            onRetryClick = {
                                viewModel.iniciarJuego()
                            }
                        )
                    }
                }
            }
        }
    }
}
