package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.CardItems.GameResultPokemonCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Hearts
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.ViewModels.common.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.games.LanguageViewModel
import com.electrofire.playpkm.ui.ViewModels.games.verificarRespuestaLanguage
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LanguageGameScreen(
    navController: NavController,
    viewModel: LanguageViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    var intentosRestantes by remember { mutableIntStateOf(3) }

    LaunchedEffect(gameStateViewModel.respondido) {
        if (gameStateViewModel.respondido) {
            val gameData = (state as? UIState.Success)?.data
            if (gameData != null) {
                if (verificarRespuestaLanguage(gameData, respuesta)) {
                    statsViewModel.registrarVictoria()
                } else {
                    statsViewModel.registrarDerrota()
                }
            }
        }
    }

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
                val gameData = currentState.data
                val pokemonActual = gameData.pokemon

                if (gameStateViewModel.respondido) {
                    val esCorrecto = verificarRespuestaLanguage(gameData, respuesta)
                    GameResultDialog(
                        isWin = esCorrecto,
                        onHomeClick = {
                            navController.navigate("home") {
                                popUpTo(Screen.LanguageGame.route) { inclusive = true }
                            }
                        },
                        content = {
                            GameResultPokemonCard(pokemon = pokemonActual)
                        }
                    )
                }

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
                                text = "POKEMON POLYGLOT",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 28.sp,
                                    lineHeight = 34.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "POKEMON POLYGLOT",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 28.sp,
                                    lineHeight = 34.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // 3 Cards con bandera y nombre del pokemon (sin etiqueta de idioma)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LanguageCard(
                                flagRes = R.drawable.japanflag,
                                pokemonName = gameData.japaneseName
                            )
                            LanguageCard(
                                flagRes = R.drawable.germanyflag,
                                pokemonName = gameData.germanName
                            )
                            LanguageCard(
                                flagRes = R.drawable.franceflag,
                                pokemonName = gameData.frenchName
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        UserInputPokemon(
                            title = "Pokemon",
                            text = respuesta,
                            onTextChange = { respuesta = it }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ConfirmButton(onConfirm = {
                            focusManager.clearFocus()
                            val esCorrecto = verificarRespuestaLanguage(gameData, respuesta)

                            if (esCorrecto || intentosRestantes <= 1) {
                                coroutineScope.launch {
                                    delay(200)
                                    if (esCorrecto) {
                                        gameStateViewModel.responder()
                                    } else {
                                        intentosRestantes--
                                        gameStateViewModel.responder()
                                    }
                                }
                            } else {
                                intentosRestantes--
                                respuesta = ""
                            }
                        })

                        Spacer(modifier = Modifier.height(32.dp))

                        Hearts(actuales = intentosRestantes)
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageCard(
    flagRes: Int,
    pokemonName: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = flagRes),
                contentDescription = null,
                modifier = Modifier.size(44.dp)
            )
            Text(
                text = pokemonName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
