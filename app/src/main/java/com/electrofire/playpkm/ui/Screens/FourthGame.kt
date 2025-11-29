package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.electrofire.playpkm.ui.Components.BannerAdd
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Contador
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.ContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.MovimientoViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaPotenciaMovimiento

@Composable
fun FourthGame(
    navController: NavController,
    viewModel: MovimientoViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    contadorViewModel: ContadorViewModel = viewModel()
) {
    var respuesta by remember { mutableStateOf("") }
    val movimientoActual = viewModel.movimiento
    var respondido by remember { mutableStateOf(false) }

    val contador = contadorViewModel.contador

    LaunchedEffect(contador) {
        if (contador == 0) {
            respondido = true
        }
    }

    Box(Modifier.fillMaxSize()) {

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!respondido) {

                Box {
                    Text(
                        text = "POWER\nOF MOVE",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 34.sp,
                            lineHeight = 38.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
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

                MovimientoCard()

                Spacer(modifier = Modifier.height(32.dp))

                UserInputPokemon(
                    title = "Potencia",
                    text = respuesta,
                    onTextChange = { respuesta = it })

                Spacer(modifier = Modifier.height(16.dp))

                ConfirmButton(onConfirm = { respondido = true })

                Spacer(modifier = Modifier.height(32.dp))

                Contador(contadorViewModel = contadorViewModel)
            } else {
                Spacer(modifier = Modifier.height(80.dp))

                MovimientoCard()

                Spacer(modifier = Modifier.height(32.dp))

                Row{
                    Box {
                        Text(
                            text = "Potencia: ",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.primary,
                                drawStyle = Stroke(width = 2f)
                            ),
                        )
                        Text(
                            text = "Potencia: ",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.secondary
                            ),
                        )
                    }
                    Text(
                        text = movimientoActual?.p.toString(),
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (verificarRespuestaPotenciaMovimiento(
                        respuesta,
                        movimientoActual
                    )
                ) {
                    WinCard(onButtonClick = {
                        navController.navigate("home") {
                            popUpTo(Screen.FourthGame.route) { inclusive = true }
                        }
                    }
                    )
                } else {
                    LoserCard(onButtonClick = {
                        navController.navigate("home") {
                            popUpTo(Screen.FourthGame.route) { inclusive = true }
                        }
                    }
                    )
                }
                LaunchedEffect(Unit) {
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
        BannerAdd(Modifier.align(alignment = Alignment.BottomStart))
    }

}
