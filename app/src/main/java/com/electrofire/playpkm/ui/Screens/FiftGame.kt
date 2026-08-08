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
import com.electrofire.playpkm.ui.CardItems.StatsApiCard
import com.electrofire.playpkm.ui.CardItems.StatsPokemonApiCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Hearts
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.FiftGameState
import com.electrofire.playpkm.ui.ViewModels.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.StatsApiViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaStatsApiPokemon

@Composable
fun FiftGame(
    navController: NavController,
    viewModel: StatsApiViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    var intentosRestantes by remember { mutableStateOf(3) }

    Box(
        Modifier.fillMaxSize()
    ) {

        GradientBackground()

        when (val currentState = state) {
            is FiftGameState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Loading()
                }
            }

            is FiftGameState.Error -> {
                if (currentState.message.contains("conexión", ignoreCase = true)) {
                    NotInternetScreen()
                } else {
                    ErrorScreen()
                }
            }

            is FiftGameState.Success -> {
                val pokemonActual = currentState.pokemon

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
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
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "MYSTERIOUS\nSTATS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 34.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
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
                        Spacer(modifier = Modifier.height(90.dp))

                        StatsPokemonApiCard(pokemon = pokemonActual)

                        Spacer(modifier = Modifier.height(64.dp))

                        if (verificarRespuestaStatsApiPokemon(pokemonActual, respuesta)) {
                            WinCard(onButtonClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.FiftGame.route) { inclusive = true }
                                }
                            }
                            )
                        } else {
                            LoserCard(onButtonClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.FiftGame.route) { inclusive = true }
                                }
                            }
                            )
                        }
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