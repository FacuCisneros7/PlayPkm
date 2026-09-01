package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.StatBestCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.common.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.ViewModels.games.TheBestViewModel
import com.electrofire.playpkm.ui.ViewModels.games.verificarRespuestaTheBest

@Composable
fun TheBestGame(
    navController: NavController,
    viewModel: TheBestViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    var selectedPokemon by remember { mutableStateOf<PokemonApi?>(null) }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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
                    NotInternetScreen(onRetry = { viewModel.loadGame() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadGame() })
                }
            }

            is UIState.Success -> {
                val gameData = currentState.data

                if (!gameStateViewModel.respondido) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box {
                            Text(
                                text = "THE BEST",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "THE BEST",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 40.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
                            modifier = Modifier.padding(horizontal = 32.dp).wrapContentSize()
                        ) {
                            Text(
                                text = "${gameData.selectedStat}".replaceFirstChar { it.uppercase() },
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.wrapContentSize().padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        gameData.pokemons.forEach { pkm ->
                            PokemonApiCard(
                                pokemon = pkm,
                                onSelected = pkm == selectedPokemon,
                                onClick = { selectedPokemon = pkm },
                                modifier = Modifier.size(152.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        ConfirmButton(
                            onConfirm = { gameStateViewModel.responder() },
                            enabled = selectedPokemon != null
                        )
                    }

                } else {
                    val esCorrecto = selectedPokemon?.let {
                        verificarRespuestaTheBest(
                            correctPokemons = gameData.correctPokemons,
                            choisePokemon = it
                        )
                    } ?: false
                    GameResultDialog(
                        isWin = esCorrecto,
                        onHomeClick = {
                            navController.navigate("home") {
                                popUpTo(Screen.TheBestGame.route) { inclusive = true }
                            }
                        },
                        content = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                GameResultPokemonCard(pokemon = gameData.correctPokemons.first())
                                Spacer(Modifier.height(16.dp))
                                StatBestCard(
                                    statNombre = "${gameData.selectedStat}",
                                    numeroStat = "${gameData.correctPokemons.first().stats[gameData.selectedStatEnglish]}"
                                )
                            }
                        }
                    )

                    LaunchedEffect(gameStateViewModel.respondido) {
                        if (esCorrecto) {
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