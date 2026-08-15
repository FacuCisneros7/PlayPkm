package com.electrofire.playpkm.ui.Screens

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
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.ui.CardItems.GameResultPokemonCard
import com.electrofire.playpkm.ui.CardItems.PokemonApiCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.HabilityCard
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.EightGameViewModel
import com.electrofire.playpkm.ui.ViewModels.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.UIState
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaEightGame

@Composable
fun EightGame(
    navController: NavController,
    viewModel: EightGameViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {

    val stateEightGame by viewModel.state.collectAsState()
    var selectedPokemon by remember { mutableStateOf<PokemonApi?>(null) }


    Box(
        Modifier.fillMaxSize()
    ) {

        when (val currentState = stateEightGame) {
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
                    NotInternetScreen(onRetry = { viewModel.loadGame() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadGame() })
                }
            }

            is UIState.Success -> {
                val state = currentState.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!gameStateViewModel.respondido) {

                        Box {
                            Text(
                                text = "IMPOSTOR",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    drawStyle = Stroke(width = 4f)
                                )
                            )
                            Text(
                                text = "IMPOSTOR",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Habilidad: ${state.abilityName}".replaceFirstChar { it.uppercase() },
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PokemonApiCard(
                                pokemon = state.pokemons[0],
                                onSelected = state.pokemons[0] == selectedPokemon,
                                onClick = { selectedPokemon = state.pokemons[0] },
                                modifier = Modifier.size(130.dp)
                            )

                            Spacer(modifier = Modifier.width(32.dp))

                            PokemonApiCard(
                                pokemon = state.pokemons[1],
                                onSelected = state.pokemons[1] == selectedPokemon,
                                onClick = { selectedPokemon = state.pokemons[1] },
                                modifier = Modifier.size(130.dp)
                            )

                        }

                        Spacer(modifier = Modifier.width(26.dp))

                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PokemonApiCard(
                                pokemon = state.pokemons[2],
                                onSelected = state.pokemons[2] == selectedPokemon,
                                onClick = { selectedPokemon = state.pokemons[2] },
                                modifier = Modifier.size(130.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(26.dp))

                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PokemonApiCard(
                                pokemon = state.pokemons[3],
                                onSelected = state.pokemons[3] == selectedPokemon,
                                onClick = { selectedPokemon = state.pokemons[3] },
                                modifier = Modifier.size(130.dp)
                            )

                            Spacer(modifier = Modifier.width(32.dp))

                            PokemonApiCard(
                                pokemon = state.pokemons[4],
                                onSelected = state.pokemons[4] == selectedPokemon,
                                onClick = { selectedPokemon = state.pokemons[4] },
                                modifier = Modifier.size(130.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        ConfirmButton(
                            onConfirm = { gameStateViewModel.responder() },
                            enabled = selectedPokemon != null
                        )
                    } else {
                        val esCorrecta = remember(gameStateViewModel.respondido){
                            selectedPokemon != null && verificarRespuestaEightGame(
                                correctPokemon = state.impostor?.name ?: "",
                                choisePokemon = selectedPokemon?.name ?: ""
                            )
                        }

                        GameResultDialog(
                            isWin = esCorrecta,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.EightGame.route) { inclusive = true }
                                }
                            },
                            content = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    GameResultPokemonCard(pokemon = state.impostor!!)

                                    Spacer(Modifier.height(16.dp))

                                    HabilityCard(pokemonActual = state.impostor)
                                }
                            }
                        )

                        LaunchedEffect(gameStateViewModel.respondido) {
                            if (esCorrecta) {
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

