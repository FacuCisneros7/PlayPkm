package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.ui.CardItems.PokemonApiCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.ViewModels.common.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.games.PyramidViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PyramidGameScreen(
    navController: NavController,
    statsViewModel: HomeStatsViewModel,
    viewModel: PyramidViewModel = hiltViewModel(),
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedIndices by viewModel.selectedIndices.collectAsState()
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is UIState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                val pokemonList = currentState.data

                if (gameStateViewModel.respondido) {
                    val esCorrecto = viewModel.verifyOrder(pokemonList)
                    val correctList = remember(pokemonList) {
                        pokemonList.sortedByDescending { it.stats.values.sum() }
                    }

                    GameResultDialog(
                        isWin = esCorrecto,
                        onHomeClick = {
                            navController.navigate("home") {
                                popUpTo(Screen.PyramidGame.route) { inclusive = true }
                            }
                        },
                        content = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "SOLUCIÓN CORRECTA",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(12.dp))

                                // Mini Pirámide de solución
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    // Top
                                    MiniPyramidCard(correctList[0])

                                    // Middle
                                    Row {
                                        MiniPyramidCard(correctList[1])
                                        MiniPyramidCard(correctList[2])
                                    }

                                    // Bottom
                                    Row {
                                        MiniPyramidCard(correctList[3])
                                        MiniPyramidCard(correctList[4])
                                        MiniPyramidCard(correctList[5])
                                    }
                                }
                            }
                        }
                    )

                    LaunchedEffect(Unit) {
                        if (esCorrecto) statsViewModel.registrarVictoria()
                        else statsViewModel.registrarDerrota()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Text(
                            text = "PYRAMID\nPOKEMON",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 34.sp,
                                lineHeight = 38.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                drawStyle = Stroke(width = 7f)
                            )
                        )
                        Text(
                            text = "PYRAMID\nPOKEMON",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 34.sp,
                                lineHeight = 38.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // Pirámide de Juego
                    PyramidRow(1, 0, pokemonList, selectedIndices) { viewModel.onCardSelected(it) }
                    Spacer(Modifier.height(8.dp))
                    PyramidRow(2, 1, pokemonList, selectedIndices) { viewModel.onCardSelected(it) }
                    Spacer(Modifier.height(8.dp))
                    PyramidRow(3, 3, pokemonList, selectedIndices) { viewModel.onCardSelected(it) }

                    Spacer(Modifier.height(32.dp))

                    ConfirmButton(onConfirm = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            delay(200)
                            gameStateViewModel.responder()
                        }
                    })

                    Spacer(Modifier.height(32.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Ordena por sus stats totales!\n(Arriba el MÁS fuerte)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniPyramidCard(pokemon: PokemonApi) {
    val bst = pokemon.stats.values.sum()
    Box(
        modifier = Modifier
            .size(70.dp)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f),
            contentScale = ContentScale.Fit
        )
        Text(
            text = bst.toString(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PyramidRow(
    count: Int,
    startIndex: Int,
    pokemonList: List<PokemonApi>,
    selectedIndices: Pair<Int?, Int?>,
    onCardClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until count) {
            val currentIndex = startIndex + i
            if (currentIndex < pokemonList.size) {
                PokemonApiCard(
                    pokemon = pokemonList[currentIndex],
                    onSelected = selectedIndices.first == currentIndex,
                    onClick = { onCardClick(currentIndex) },
                    modifier = Modifier.size(110.dp)
                )
            }
        }
    }
}
