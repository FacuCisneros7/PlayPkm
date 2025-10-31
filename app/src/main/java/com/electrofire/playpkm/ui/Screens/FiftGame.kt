package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.PokemonStatsCard
import com.electrofire.playpkm.ui.CardItems.StatPokemonCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Hearts
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.PokemonViewModel
import com.electrofire.playpkm.ui.ViewModels.StatsViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaStatsPokemon

@Composable
fun FiftGame(navController: NavController, viewModel: StatsViewModel = hiltViewModel(), statsViewModel: HomeStatsViewModel) {
    var respuesta by remember { mutableStateOf("") }
    val pokemonActual = viewModel.pokemon
    var respondido by remember { mutableStateOf(false) }

    var intentosRestantes by remember { mutableStateOf(3) } // 👈 contador de vidas

    Box(Modifier.fillMaxSize()) {

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!respondido) {

                Image(
                    painter = painterResource(id = R.drawable.segundojuego),
                    contentDescription = null,
                    modifier = Modifier.height(80.dp).wrapContentWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                StatPokemonCard()

                Spacer(modifier = Modifier.height(32.dp))

                UserInputPokemon(
                    title = "Pokemon",
                    text = respuesta,
                    onTextChange = { respuesta = it })

                Spacer(modifier = Modifier.height(32.dp))

                ConfirmButton(onConfirm = {
                    if (verificarRespuestaStatsPokemon(pokemonActual, respuesta)) {
                        respondido = true // ✅ acertó, termina el juego
                    } else {
                        intentosRestantes--
                        if (intentosRestantes <= 0) {
                            respondido = true // ❌ perdió todos los intentos
                        } else {
                            respuesta = "" // 🔄 limpiar input y permitir otro intento
                        }
                    }
                })

                Spacer(modifier = Modifier.height(100.dp))

                Hearts(actuales = intentosRestantes)
            } else{
                Text(
                    text = "El pokemon es...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                PokemonStatsCard()

                Spacer(modifier = Modifier.height(32.dp))

                if (verificarRespuestaStatsPokemon(pokemonActual, respuesta)) {
                    WinCard(onButtonClick = { navController.navigate("home") })
                } else {
                    LoserCard(onButtonClick = { navController.navigate("home") })
                }
                LaunchedEffect(Unit) {
                    if (verificarRespuestaStatsPokemon(pokemonActual, respuesta)) {
                        statsViewModel.registrarVictoria()
                    } else {
                        statsViewModel.registrarDerrota()
                    }
                }
            }
        }

    }
}