package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.ui.CardItems.PokemonApiCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.SeventhGameViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaSeventhGame

@Composable
fun SeventhGame(
    navController: NavController,
    viewModel: SeventhGameViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel
) {

    val state by viewModel.state.collectAsState()

    var selectedPokemon by remember { mutableStateOf<PokemonApi?>(null) }

    var respondido by remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        GradientBackground()

        if (state.pokemons.isNotEmpty() && state.selectedStat != null) {

            if (!respondido) {

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
                                color = MaterialTheme.colorScheme.primary,
                                drawStyle = Stroke(width = 6f)
                            )
                        )
                        Text(
                            text = "THE BEST",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 40.sp,
                                lineHeight = 38.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${state.selectedStat}".replaceFirstChar { it.uppercase() },
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    state.pokemons.forEach { pkm ->
                        PokemonApiCard(
                            pokemon = pkm,
                            onSelected = pkm == selectedPokemon,
                            onClick = { selectedPokemon = pkm },
                            modifier = Modifier.size(152.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ConfirmButton(
                        onConfirm = { respondido = true },
                        enabled = selectedPokemon != null
                    )
                }

            } else {

                Column(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .height(180.dp)
                            .padding(8.dp),
                        border = BorderStroke(4.dp, Color(0xFF00C853)),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = state.correctPokemon?.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(150.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    Spacer(Modifier.height(32.dp))

                    Text(
                        text = "${state.selectedStat}: ${state.correctPokemon!!.stats[state.selectedStatEnglish]}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(32.dp))

                    if (verificarRespuestaSeventhGame(
                            correctPokemon = state.correctPokemon!!,
                            choisePokemon = selectedPokemon!!
                        )
                    ) {
                        WinCard(onButtonClick = {
                            navController.navigate("home") {
                                popUpTo(Screen.SeventhGame.route) { inclusive = true }
                            }
                        }
                        )
                    } else {
                        LoserCard(onButtonClick = {
                            navController.navigate("home") {
                                popUpTo(Screen.SeventhGame.route) { inclusive = true }
                            }
                        }
                        )
                    }

                    LaunchedEffect(Unit) {
                        if (verificarRespuestaSeventhGame(
                                correctPokemon = state.correctPokemon!!,
                                choisePokemon = selectedPokemon!!
                            )
                        ) {
                            statsViewModel.registrarVictoria()
                        } else {
                            statsViewModel.registrarDerrota()
                        }
                    }

                }
            }
        } else {
            Loading()
        }
    }
}
