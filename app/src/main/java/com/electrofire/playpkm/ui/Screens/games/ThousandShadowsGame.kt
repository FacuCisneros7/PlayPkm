package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.CardItems.SombrasInfCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.ContadorCorto
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.ViewModels.games.ThousandShadowsViewModel

@Composable
fun ThousandShadowsGame(
    navController: NavController,
    viewModel: ThousandShadowsViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel
) {
    val usuario = statsViewModel.userData
    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val contador by viewModel.contador.collectAsState()

    LaunchedEffect(state) {
        if (state is UIState.Success && (state as UIState.Success).data.isGameOver) {
            statsViewModel.registrarMaxScoreElevenGame((state as UIState.Success).data.puntaje)
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {

        when (val currentState = state) {
            is UIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading()
                }
            }

            is UIState.Error -> {
                if (currentState.message.contains("conexión", ignoreCase = true)) {
                    NotInternetScreen(onRetry = { viewModel.iniciarJuego() })
                } else {
                    ErrorScreen(onRetry = { viewModel.iniciarJuego() })
                }
            }

            is UIState.Success -> {
                val gameData = currentState.data
                val pokemonActual = gameData.pokemon
                val puntaje = gameData.puntaje
                val isGameOver = gameData.isGameOver

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp)
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!isGameOver) {

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CatchingPokemon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(id = R.string.ninthgame_puntuation),
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = " $puntaje",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        ContadorCorto(contador = contador)

                        Spacer(Modifier.height(16.dp))

                        SombrasInfCard(pokemon = pokemonActual)

                        Spacer(modifier = Modifier.height(24.dp))

                        UserInputPokemon(
                            title = "Pokemon",
                            text = respuesta,
                            onTextChange = { respuesta = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ConfirmButton(
                            onConfirm = {
                                viewModel.escribirPokemon(respuesta)
                                respuesta = ""
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Box {
                            Text(
                                text = "THOUSAND SHADOWS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "THOUSAND SHADOWS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    } else {
                        GameResultDialog(
                            isGameOver = true,
                            puntaje = puntaje,
                            maxScore = usuario.maxPointsDos,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.ThousandShadowsGame.route) { inclusive = true }
                                }
                            },
                            onRetryClick = {
                                viewModel.iniciarJuego()
                            }
                        )
                    }
                }
            }
        }
    }
}