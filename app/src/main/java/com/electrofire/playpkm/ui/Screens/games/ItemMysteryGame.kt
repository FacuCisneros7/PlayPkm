package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.ItemApi
import com.electrofire.playpkm.ui.CardItems.GameResultItemCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Hearts
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.ViewModels.common.GameStateViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.games.ItemMysteryViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@Composable
fun ItemMysteryGame(
    navController: NavController,
    viewModel: ItemMysteryViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    gameStateViewModel: GameStateViewModel = hiltViewModel()
) {
    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    var intentosRestantes by remember { mutableIntStateOf(3) }

    // Registrar resultado UNA SOLA VEZ cuando se responde
    LaunchedEffect(gameStateViewModel.respondido) {
        if (gameStateViewModel.respondido) {
            val itemActual = (state as? UIState.Success)?.data
            if (itemActual != null) {
                if (viewModel.verifyAnswer(itemActual, respuesta)) {
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
                    NotInternetScreen(onRetry = { viewModel.loadItem() })
                } else {
                    ErrorScreen(onRetry = { viewModel.loadItem() })
                }
            }

            is UIState.Success -> {
                val itemActual = currentState.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!gameStateViewModel.respondido) {
                        // INTERFAZ DE JUEGO ACTIVA
                        Box {
                            Text(
                                text = "ITEM MYSTERY",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 7f)
                                )
                            )
                            Text(
                                text = "ITEM MYSTERY",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 32.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        ItemSpriteCard(item = itemActual)

                        Spacer(modifier = Modifier.height(32.dp))

                        OutlinedTextField(
                            value = respuesta,
                            onValueChange = { respuesta = it },
                            placeholder = {
                                Text(
                                    text = "Nombre del item",
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                            },
                            modifier = Modifier.width(200.dp),
                            textStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                                unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(0.6f),
                                cursorColor = MaterialTheme.colorScheme.tertiary,
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                                focusedPlaceholderColor = MaterialTheme.colorScheme.primary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ConfirmButton(onConfirm = {
                            if (viewModel.verifyAnswer(itemActual, respuesta)) {
                                gameStateViewModel.responder()
                            } else {
                                intentosRestantes--
                                if (intentosRestantes <= 0) {
                                    gameStateViewModel.responder()
                                } else {
                                    respuesta = ""
                                }
                            }
                        })

                        Spacer(modifier = Modifier.height(48.dp))

                        Hearts(actuales = intentosRestantes)

                    } else {
                        // PANTALLA DE RESULTADOS (DIFERENCIADA AL IGUAL QUE OTROS JUEGOS)
                        val esCorrecto = viewModel.verifyAnswer(itemActual, respuesta)
                        
                        GameResultDialog(
                            isWin = esCorrecto,
                            onHomeClick = {
                                navController.navigate("home") {
                                    popUpTo(Screen.ItemMysteryGame.route) { inclusive = true }
                                }
                            },
                            content = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    GameResultItemCard(item = itemActual)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = itemActual.name.uppercase(),
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemSpriteCard(item: ItemApi) {
    Card(
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp),
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AsyncImage(
                model = item.sprite,
                contentDescription = null,
                modifier = Modifier.size(170.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
