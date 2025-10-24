package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.DosPokemonCard
import com.electrofire.playpkm.ui.CardItems.DosSilhouettePokemonCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Hearts
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.UserInputPokemonDos
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModel
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModelDos
import com.electrofire.playpkm.ui.ViewModels.DosSiluetasPokemonViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaDosSiluetas

@Composable
fun SeventhGame(navController: NavController, viewModel: DosSiluetasPokemonViewModel = hiltViewModel(),
                statsViewModel: HomeStatsViewModel)
{
    var respuesta by remember { mutableStateOf("") }
    var respuestaDos by remember { mutableStateOf("") }
    val pokemonActualUno = viewModel.pokemonUno
    val pokemonActualDos = viewModel.pokemonDos
    var respondido by remember { mutableStateOf(false) }

    val viewModel1: AutoPokeViewModel = hiltViewModel()
    val viewModel2: AutoPokeViewModelDos = hiltViewModel()

    var intentosRestantes by remember { mutableStateOf(3) } // 👈 contador de vidas

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!respondido) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¿Quienes son los 2 pokemon?",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                DosSilhouettePokemonCard()

                Spacer(modifier = Modifier.height(32.dp))

                UserInputPokemon(
                    title = "Pokemon 1",
                    text = respuesta,
                    onTextChange = { respuesta = it },
                    viewModel = viewModel1)

                Spacer(modifier = Modifier.height(16.dp))

                UserInputPokemonDos(
                    title = "Pokemon 2",
                    text = respuestaDos,
                    onTextChange = { respuestaDos = it },
                    viewModel = viewModel2)

                Spacer(modifier = Modifier.height(32.dp))

                ConfirmButton(onConfirm = {if (verificarRespuestaDosSiluetas(pokemonActualUno, pokemonActualDos, respuesta, respuestaDos)) {
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

                Spacer(modifier = Modifier.height(32.dp))

                Hearts(actuales = intentosRestantes)

            } else{
                Text(
                    text = "Los pokemon son...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                DosPokemonCard()

                Spacer(modifier = Modifier.height(32.dp))

                if (verificarRespuestaDosSiluetas(pokemonActualUno, pokemonActualDos, respuesta, respuestaDos)) {
                    WinCard(onButtonClick = { navController.navigate("home") })
                } else {
                    LoserCard(onButtonClick = { navController.navigate("home") })
                }
                LaunchedEffect(Unit) {
                    if (verificarRespuestaDosSiluetas(pokemonActualUno, pokemonActualDos,respuesta, respuestaDos)) {
                        statsViewModel.registrarVictoria()
                    } else {
                        statsViewModel.registrarDerrota()
                    }
                }
            }
        }

    }
}