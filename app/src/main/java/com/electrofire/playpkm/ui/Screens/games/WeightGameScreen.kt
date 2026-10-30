package com.electrofire.playpkm.ui.Screens.games

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.games.WeightGameViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@Composable
fun WeightGameScreen(
    navController: NavController,
    viewModel: WeightGameViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var resultRegistered by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is UIState.Success) {
            val data = (state as UIState.Success).data
            if (data.isGameOver && !resultRegistered) {
                resultRegistered = true
                if (data.isWin) {
                    statsViewModel.registrarVictoria()
                } else {
                    statsViewModel.registrarDerrota()
                }
            } else if (!data.isGameOver) {
                resultRegistered = false
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is UIState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                val currentStep = gameData.currentStep
                val isAnswerRevealed = gameData.isAnswerRevealed
                val isLoadingNextStep = gameData.isLoadingNextStep
                val isGameOver = gameData.isGameOver
                val isWin = gameData.isWin

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!isGameOver) {
                        Box {
                            Text(
                                text = "POKE HEAVY",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "POKE HEAVY",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // 5 Circles Step Indicator
                        StepIndicator(currentStep = currentStep)

                        Spacer(Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.padding(horizontal = 32.dp)
                        ) {
                            Text(
                                text = "¡Selecciona al Pokémon más PESADO!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        // Pokemon A Card
                        Card(
                            modifier = Modifier
                                .width(170.dp)
                                .height(170.dp)
                                .padding(8.dp)
                                .clickable(enabled = !isAnswerRevealed && !isLoadingNextStep) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }
                                    viewModel.seleccionarPokemon(pokemonA)
                                },
                            border = BorderStroke(4.dp, MaterialTheme.colorScheme.outline),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = pokemonA.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(140.dp),
                                    contentScale = ContentScale.Fit
                                )
                                if (isAnswerRevealed) {
                                    Text(
                                        text = "${pokemonA.weight / 10.0} kg",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 8.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.inverseSurface.copy(
                                                    alpha = 0.6f
                                                ),
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // Pokemon B Card
                        Card(
                            modifier = Modifier
                                .width(170.dp)
                                .height(170.dp)
                                .padding(8.dp)
                                .clickable(enabled = !isAnswerRevealed && !isLoadingNextStep) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }
                                    viewModel.seleccionarPokemon(pokemonB)
                                },
                            border = BorderStroke(4.dp, MaterialTheme.colorScheme.onSurface),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = pokemonB.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(140.dp),
                                    contentScale = ContentScale.Fit
                                )
                                if (isAnswerRevealed) {
                                    Text(
                                        text = "${pokemonB.weight / 10.0} kg",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 8.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.inverseSurface.copy(
                                                    alpha = 0.6f
                                                ),
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        if (isAnswerRevealed) {
                            Button(
                                onClick = { viewModel.continuarSiguientePaso() },
                                enabled = !isLoadingNextStep,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                modifier = Modifier.padding(horizontal = 32.dp)
                            ) {
                                Text(
                                    text = if (isLoadingNextStep) "CARGANDO..." else "CONTINUAR",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        }

                    } else {
                        GameResultDialog(
                            isWin = isWin,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.WeightGame.route) { inclusive = true }
                                }
                            },
                            content = {
                                WeightComparisonResultCard(
                                    pokemonA = gameData.finalPokemonA ?: pokemonA,
                                    pokemonB = gameData.finalPokemonB ?: pokemonB
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeightComparisonResultCard(pokemonA: PokemonApi, pokemonB: PokemonApi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = pokemonA.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${pokemonA.weight / 10.0} kg",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "VS",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
            color = MaterialTheme.colorScheme.tertiary
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = pokemonB.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${pokemonB.weight / 10.0} kg",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int, totalSteps: Int = 5) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isCompleted = i < currentStep
            val isCurrent = i == currentStep
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = when {
                            isCompleted -> Color(0xFF4CAF50) // Verde completado
                            isCurrent -> MaterialTheme.colorScheme.tertiary // Actual
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) // Futuro
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                (if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = "$i",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary
                        )
                    )
                })
            }
        }
    }
}
