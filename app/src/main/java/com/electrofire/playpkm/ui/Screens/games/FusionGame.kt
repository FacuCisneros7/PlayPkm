package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.electrofire.playpkm.ui.CardItems.PokemonesFusionCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.UserInputPokemonDos
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.common.AutoPokeViewModel
import com.electrofire.playpkm.ui.ViewModels.common.AutoPokeViewModelDos
import com.electrofire.playpkm.ui.ViewModels.games.FusionViewModel
import com.electrofire.playpkm.ui.ViewModels.common.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.games.verificarRespuestaFusion
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen

@Composable
fun FusionGame(
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
                    NotInternetScreen(onRetry = { viewModel.loadFusion() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadFusion() })
                }
            }

            is UIState.Success -> {
                val fusionActual = currentState.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
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
                                    color = MaterialTheme.colorScheme.tertiary,
                                    drawStyle = Stroke(width = 4f)
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
                        val esCorrecto = verificarRespuestaFusion(fusionActual, respuesta, respuestaDos)
                        GameResultDialog(
                            isWin = esCorrecto,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.FusionGame.route) { inclusive = true }
                                }
                            },
                            content = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    FusionCard(fusion = fusionActual)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    PokemonesFusionCard(fusionActual = fusionActual)
                                }
                            }
                        )
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