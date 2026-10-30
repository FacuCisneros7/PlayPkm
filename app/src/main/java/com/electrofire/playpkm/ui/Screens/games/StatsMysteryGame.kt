package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.electrofire.playpkm.Data.PokemonTypeUtils
import com.electrofire.playpkm.ui.CardItems.GameResultPokemonCard
import com.electrofire.playpkm.ui.CardItems.StatsApiCard
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
import com.electrofire.playpkm.ui.ViewModels.games.StatsMysteryViewModel
import com.electrofire.playpkm.ui.ViewModels.games.verificarRespuestaStatsApiPokemon
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StatsMysteryGame(
    navController: NavController,
    viewModel: StatsMysteryViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    var intentosRestantes by remember { mutableIntStateOf(3) }

    // Registrar resultado UNA SOLA VEZ cuando se responde
    LaunchedEffect(gameStateViewModel.respondido) {
        if (gameStateViewModel.respondido) {
            val pokemonActual = (state as? UIState.Success)?.data
            if (pokemonActual != null) {
                if (verificarRespuestaStatsApiPokemon(pokemonActual, respuesta)) {
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading()
                }
            }

            is UIState.Error -> {
                if (currentState.message.contains("conexión", ignoreCase = true)) {
                    NotInternetScreen(onRetry = { viewModel.loadPokemon() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadPokemon() })
                }
            }

            is UIState.Success -> {
                val pokemonActual = currentState.data

                if (gameStateViewModel.respondido) {
                    val esCorrecto = verificarRespuestaStatsApiPokemon(pokemonActual, respuesta)
                    GameResultDialog(
                        isWin = esCorrecto,
                        onHomeClick = {
                            navController.navigate("home") {
                                popUpTo(Screen.StatsMysteryGame.route) { inclusive = true }
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
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "MYSTERIOUS\nSTATS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 34.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        StatsApiCard(pokemon = pokemonActual)

                        Spacer(modifier = Modifier.height(32.dp))

                        if (intentosRestantes < 3) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "PISTA 1: NºPOKÉDEX",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${pokemonActual.id}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.primary.copy(0.8f),
                                            textAlign = TextAlign.Center
                                        )

                                        if (intentosRestantes < 2) {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            HorizontalDivider(
                                                color = MaterialTheme.colorScheme.primary.copy(
                                                    alpha = 0.1f
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))

                                            Text(
                                                text = "PISTA 2: TIPOS",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                pokemonActual.types.keys.forEach { typeName ->
                                                    Image(
                                                        painter = painterResource(
                                                            id = PokemonTypeUtils.getTypeRes(
                                                                typeName
                                                            )
                                                        ),
                                                        contentDescription = typeName,
                                                        modifier = Modifier
                                                            .size(30.dp)
                                                            .padding(horizontal = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        UserInputPokemon(
                            title = "Pokemon",
                            text = respuesta,
                            onTextChange = { respuesta = it })

                        Spacer(modifier = Modifier.height(16.dp))

                        ConfirmButton(onConfirm = {
                            focusManager.clearFocus()
                            val esCorrecto =
                                verificarRespuestaStatsApiPokemon(pokemonActual, respuesta)

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

                        Spacer(modifier = Modifier.height(48.dp))

                        Hearts(actuales = intentosRestantes)
                    }
                }
            }
        }
    }
}
