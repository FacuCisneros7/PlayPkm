package com.electrofire.playpkm.ui.Screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.MovimientoCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Contador
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.PotenciaCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.ContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.MovimientoViewModel
import com.electrofire.playpkm.ui.ViewModels.UIState
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaPotenciaMovimiento

@Composable
fun FourthGame(
    navController: NavController,
    viewModel: MovimientoViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
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
                    NotInternetScreen(onRetry = { viewModel.loadMovimiento() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadMovimiento() })
                }
            }

            is UIState.Success -> {
                val movimientoActual = currentState.data

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
                                text = "POWER\nOF MOVE",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 34.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    drawStyle = Stroke(width = 4f)
                                )
                            )
                            Text(
                                text = "POWER\nOF MOVE",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 34.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        MovimientoCard(movimiento = movimientoActual)

                        Spacer(modifier = Modifier.height(32.dp))

                        UserInputPokemon(
                            title = "Potencia",
                            text = respuesta,
                            onTextChange = { respuesta = it })

                        Spacer(modifier = Modifier.height(16.dp))

                        ConfirmButton(onConfirm = { gameStateViewModel.responder() })

                        Spacer(modifier = Modifier.height(32.dp))

                        Contador(contadorViewModel = contadorViewModel)
                    } else {
                        val esCorrecto = verificarRespuestaPotenciaMovimiento(respuesta, movimientoActual)
                        GameResultDialog(
                            isWin = esCorrecto,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.FourthGame.route) { inclusive = true }
                                }
                            },
                            content = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    MovimientoCard(movimiento = movimientoActual)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    PotenciaCard(movimiento = movimientoActual)
                                }
                            }
                        )
                        LaunchedEffect(gameStateViewModel.respondido) {
                            if (verificarRespuestaPotenciaMovimiento(
                                    respuesta,
                                    movimientoActual
                                )
                            ) {
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
