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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.PokemonZoom
import com.electrofire.playpkm.ui.CardItems.PokemonZoomTrue
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Contador
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.ContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.TenGameState
import com.electrofire.playpkm.ui.ViewModels.TenGameViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaPokemonConZoom

@Composable
fun TenGame(
    navController: NavController,
    statsViewModel: HomeStatsViewModel,
    viewModel: TenGameViewModel = hiltViewModel(),
    contadorViewModel: ContadorViewModel = viewModel(),
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {

    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    val contador = contadorViewModel.contador

    LaunchedEffect(contador) {
        if (contador == 0) {
            gameStateViewModel.responder()
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {

        GradientBackground()

        when (val currentState = state) {
            is TenGameState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Loading()
                }
            }

            is TenGameState.Error -> {
                if (currentState.message.contains("conexión", ignoreCase = true)) {
                    NotInternetScreen()
                } else {
                    ErrorScreen()
                }
            }

            is TenGameState.Success -> {
                val pokemonActual = currentState.pokemon

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!gameStateViewModel.respondido) {

                        Box {
                            Text(
                                text = "ZOOM GAME",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "ZOOM GAME",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        PokemonZoom(pokemon = pokemonActual)

                        Spacer(modifier = Modifier.height(48.dp))

                        UserInputPokemon(
                            title = "Pokemon",
                            text = respuesta,
                            onTextChange = { respuesta = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ConfirmButton(onConfirm = { gameStateViewModel.responder() })

                        Spacer(modifier = Modifier.height(32.dp))

                        Contador(contadorViewModel = contadorViewModel)
                    } else {

                        Spacer(modifier = Modifier.height(90.dp))

                        PokemonZoomTrue(pokemon = pokemonActual)

                        Spacer(modifier = Modifier.height(64.dp))

                        if (verificarRespuestaPokemonConZoom(pokemonActual, respuesta)) {
                            WinCard(onButtonClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.FirstGame.route) { inclusive = true }
                                }
                            }
                            )
                        } else {
                            LoserCard(onButtonClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.FirstGame.route) { inclusive = true }
                                }
                            }
                            )
                        }

                        LaunchedEffect(gameStateViewModel.respondido) {
                            if (verificarRespuestaPokemonConZoom(pokemonActual, respuesta)) {
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