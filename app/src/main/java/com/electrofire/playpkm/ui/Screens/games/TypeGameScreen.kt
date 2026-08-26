package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.GameResultPokemonCard
import com.electrofire.playpkm.ui.CardItems.PokemonApiCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Contador
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.TypeDropdown
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.ViewModels.common.ContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.common.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.Data.PokemonTypeUtils
import com.electrofire.playpkm.ui.ViewModels.games.TypeGameViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@Composable
fun TypeGameScreen(
    navController: NavController,
    viewModel: TypeGameViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    contadorViewModel: ContadorViewModel = viewModel(),
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    var selectedType1 by remember { mutableStateOf<String?>(null) }
    var selectedType2 by remember { mutableStateOf<String?>(null) }
    val state by viewModel.state.collectAsState()

    val contador = contadorViewModel.contador

    LaunchedEffect(contador) {
        if (contador == 0) {
            gameStateViewModel.responder()
        }
    }

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
                    NotInternetScreen(onRetry = { viewModel.loadRandomPokemon() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadRandomPokemon() })
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!gameStateViewModel.respondido) {

                        Box {
                            Text(
                                text = "TYPES",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "TYPES",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        PokemonApiCard(
                            pokemon = pokemonActual,
                            onSelected = false,
                            onClick = {},
                            modifier = Modifier.height(200.dp).width(200.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TypeDropdown(
                            label = "Tipo 1",
                            selectedType = selectedType1,
                            onTypeSelected = { selectedType1 = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TypeDropdown(
                            label = "Tipo 2 (Opc.)",
                            selectedType = selectedType2,
                            onTypeSelected = { selectedType2 = it },
                            showEmpty = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ConfirmButton(onConfirm = { 
                            if (selectedType1 != null && selectedType2 != null) {
                                gameStateViewModel.responder() 
                            }
                        })

                        Spacer(modifier = Modifier.height(32.dp))

                        Contador(contadorViewModel = contadorViewModel)
                    } else {
                        val esCorrecto = viewModel.checkAnswer(
                            pokemonActual, 
                            selectedType1 ?: "", 
                            selectedType2 ?: "No tiene"
                        )
                        
                        GameResultDialog(
                            isWin = esCorrecto,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo("type_game") { inclusive = true }
                                }
                            },
                            content = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    GameResultPokemonCard(pokemon = pokemonActual)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    val t1 = pokemonActual.types.entries.find { it.value == 1 }?.key
                                    val t2 = pokemonActual.types.entries.find { it.value == 2 }?.key

                                    val typeResOne = PokemonTypeUtils.getTypeRes(t1)
                                    val typeResTwo = PokemonTypeUtils.getTypeRes(t2)

                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Image(
                                            painter = painterResource(id = typeResOne),
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp)
                                        )

                                        Spacer(modifier = Modifier.width(30.dp))

                                        Image(
                                            painter = painterResource(id = typeResTwo),
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }

                                }
                            }
                        )

                        LaunchedEffect(Unit) {
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
}
