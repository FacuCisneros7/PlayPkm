package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.FusionCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.PokemonesFusionCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.UserInputPokemonDos
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModel
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModelDos
import com.electrofire.playpkm.ui.ViewModels.FusionViewModel
import com.electrofire.playpkm.ui.ViewModels.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.SixthGameState
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaFusion

@Composable
fun SixthGame(
    navController: NavController,
    viewModel1: AutoPokeViewModel = hiltViewModel(),
    viewModel2: AutoPokeViewModelDos = hiltViewModel(),
    viewModel: FusionViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    var respuesta by remember { mutableStateOf("") }
    var respuestaDos by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    Box(Modifier.fillMaxSize()) {

        GradientBackground()

        when (val currentState = state) {
            is SixthGameState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Loading()
                }
            }

            is SixthGameState.Error -> {
                if (currentState.message.contains("conexión", ignoreCase = true)) {
                    NotInternetScreen()
                } else {
                    ErrorScreen()
                }
            }

            is SixthGameState.Success -> {
                val fusionActual = currentState.fusion

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (!gameStateViewModel.respondido) {

                        Box {
                            Text(
                                text = "FUSION!",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "FUSION!",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        FusionCard(fusion = fusionActual)

                        Spacer(modifier = Modifier.height(16.dp))

                        UserInputPokemon(
                            title = "Pokemon 1",
                            text = respuesta,
                            onTextChange = { respuesta = it },
                            viewModel = viewModel1
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        UserInputPokemonDos(
                            title = "Pokemon 2",
                            text = respuestaDos,
                            onTextChange = { respuestaDos = it },
                            viewModel = viewModel2
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        ConfirmButton(onConfirm = { gameStateViewModel.responder() })
                    } else {
                        Spacer(modifier = Modifier.height(32.dp))

                        FusionCard(fusion = fusionActual)

                        Spacer(modifier = Modifier.height(16.dp))

                        PokemonesFusionCard(fusionActual = fusionActual)

                        Spacer(modifier = Modifier.height(32.dp))

                        if (verificarRespuestaFusion(fusionActual, respuesta, respuestaDos)) {
                            WinCard(onButtonClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.SixthGame.route) { inclusive = true }
                                }
                            }
                            )
                        } else {
                            LoserCard(onButtonClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.SixthGame.route) { inclusive = true }
                                }
                            }
                            )
                        }
                        LaunchedEffect(gameStateViewModel.respondido) {
                            if (verificarRespuestaFusion(fusionActual, respuesta, respuestaDos)) {
                                statsViewModel.registrarVictoria()
                            } else {
                                statsViewModel.registrarDerrota()
                            }
                        }
                    }
                }
            }
        }
    }
}