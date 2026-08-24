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
import com.electrofire.playpkm.ui.CardItems.GameResultPokemonCard
import com.electrofire.playpkm.ui.CardItems.StatsApiCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Hearts
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.common.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.games.verificarRespuestaStatsApiPokemon
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.ViewModels.games.StatsMysteryViewModel

@Composable
fun StatsMysteryGame(
    navController: NavController,
        viewModel: StatsMysteryViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    var intentosRestantes by remember { mutableStateOf(3) }

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
                    NotInternetScreen(onRetry = { viewModel.loadPokemon() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadPokemon() })
                }
            }

            is UIState.Success -> {
                val pokemonActual = currentState.data

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
                                text = "MYSTERIOUS\nSTATS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 34.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "MYSTERIOUS\nSTATS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 34.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        StatsApiCard(pokemon = pokemonActual)

                        Spacer(modifier = Modifier.height(32.dp))

                        UserInputPokemon(
                            title = "Pokemon",
                            text = respuesta,
                            onTextChange = { respuesta = it })

                        Spacer(modifier = Modifier.height(16.dp))

                        ConfirmButton(onConfirm = {
                            if (verificarRespuestaStatsApiPokemon(pokemonActual, respuesta)) {
                                gameStateViewModel.responder()
                            } else {
                                intentosRestantes--
                                if (intentosRestantes <= 0) {
                                    gameStateViewModel.responder()
                                } else {
                                    respuesta = ""
                                }
                            }
                        })

                        Spacer(modifier = Modifier.height(48.dp))

                        Hearts(actuales = intentosRestantes)
                    } else {
                        val esCorrecto = verificarRespuestaStatsApiPokemon(pokemonActual, respuesta)
                        GameResultDialog(
                            isWin = esCorrecto,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.StatsMysteryGame.route) { inclusive = true }
                                }
                            },
                            content = {
                                GameResultPokemonCard(pokemon = pokemonActual)
                            }
                        )
                        LaunchedEffect(gameStateViewModel.respondido) {
                            if (verificarRespuestaStatsApiPokemon(pokemonActual, respuesta)) {
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