package com.electrofire.playpkm.ui.Screens.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.GameResultPokemonCard
import com.electrofire.playpkm.ui.Components.GameResultDialog
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.Screens.error.ErrorScreen
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.games.LetterColor
import com.electrofire.playpkm.ui.ViewModels.games.PokewordleGameState
import com.electrofire.playpkm.ui.ViewModels.games.PokewordleViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@Composable
fun PokewordleGame(
    navController: NavController,
    viewModel: PokewordleViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel
) {
    val state by viewModel.state.collectAsState()
    val wordLength = viewModel.wordLength
    val guesses = viewModel.guesses.value
    val guessColors = viewModel.guessColors.value
    val gameStatus = viewModel.gameStatus
    val keyboardColors = viewModel.keyboardColors.value

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                val pokemon = currentState.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Título
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box {
                            Text(
                                text = "POKEWORDLE",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 40.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "POKEWORDLE",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 40.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    // Cuadrícula (Grid 6xN)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        for (r in 0 until 6) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                for (c in 0 until wordLength) {
                                    val char = if (r < guesses.size && c < guesses[r].size) guesses[r][c] else ' '
                                    val color = if (r < guessColors.size && c < guessColors[r].size) guessColors[r][c] else LetterColor.UNUSED

                                    val bgColor = when (color) {
                                        LetterColor.GREEN -> Color(0xFF4CAF50)
                                        LetterColor.YELLOW -> Color(0xFFFF9800)
                                        LetterColor.RED -> Color(0xFFE53935)
                                        LetterColor.UNUSED -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(bgColor)
                                            .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = char.toString(),
                                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Teclado Virtual
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val row1 = listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P')
                        val row2 = listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L')
                        val row3 = listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M', '-')

                        KeyboardRow(row1, keyboardColors) { viewModel.appendLetter(it) }
                        KeyboardRow(row2, keyboardColors) { viewModel.appendLetter(it) }
                        KeyboardRow(row3, keyboardColors) { viewModel.appendLetter(it) }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Botones de Acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.deleteAll() },
                                modifier = Modifier.weight(1f).height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("BORRAR TODO", style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.primary)
                            }

                            Button(
                                onClick = { viewModel.deleteLetter() },
                                modifier = Modifier.weight(1f).height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("BORRAR", style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.primary)
                            }

                            Button(
                                onClick = { viewModel.confirmGuess() },
                                modifier = Modifier.weight(1.2f).height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("CONFIRMAR", style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                // Diálogos de Victoria o Derrota
                if (gameStatus is PokewordleGameState.Won || gameStatus is PokewordleGameState.Lost) {
                    val isWin = gameStatus is PokewordleGameState.Won
                    GameResultDialog(
                        isWin = isWin,
                        onHomeClick = {
                            navController.navigate("home") {
                                popUpTo(Screen.PokewordleGame.route) { inclusive = true }
                            }
                        },
                        content = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                GameResultPokemonCard(pokemon = pokemon)
                            }
                        }
                    )

                    LaunchedEffect(gameStatus) {
                        if (isWin) {
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

@Composable
fun KeyboardRow(chars: List<Char>, keyboardColors: Map<Char, LetterColor>, onClick: (Char) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        chars.forEach { char ->
            val color = keyboardColors[char]
            val bgColor = when (color) {
                LetterColor.GREEN -> Color(0xFF4CAF50)
                LetterColor.YELLOW -> Color(0xFFFF9800)
                LetterColor.RED -> Color(0xFF555555)
                else -> MaterialTheme.colorScheme.secondary
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(bgColor)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .clickable { onClick(char) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}
