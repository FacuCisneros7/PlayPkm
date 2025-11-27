package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.electrofire.playpkm.ui.CardItems.HabilityPokemonCard
import com.electrofire.playpkm.ui.Components.BannerAdd
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Contador
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.HabilityCard
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.ContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.HabilityViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaHabilidadPokemon

@Composable
fun ThirdGame(
    navController: NavController,
    viewModel: HabilityViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel,
    contadorViewModel: ContadorViewModel = viewModel()
) {
    var respuesta by remember { mutableStateOf("") }
    val pokemonActual = viewModel.pokemon
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
                        text = "ONE ABILITY",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            lineHeight = 38.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    // Relleno
                    Text(
                        text = "ONE ABILITY",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            lineHeight = 38.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HabilityPokemonCard()

                Spacer(modifier = Modifier.height(32.dp))

                UserInputPokemon(
                    title = "Habilidad",
                    text = respuesta,
                    onTextChange = { respuesta = it })

                Spacer(modifier = Modifier.height(16.dp))

                ConfirmButton(onConfirm = { respondido = true })

                Spacer(modifier = Modifier.height(32.dp))

                Contador(contadorViewModel = contadorViewModel)
            } else {
                Spacer(modifier = Modifier.height(40.dp))

                HabilityPokemonCard()

                Spacer(modifier = Modifier.height(16.dp))

                HabilityCard(pokemonActual = pokemonActual)

                Spacer(modifier = Modifier.height(16.dp))

                if (verificarRespuestaHabilidadPokemon(
                        pokemonActual,
                        respuesta
                    )
                ) {  //verificarRespuesta por verificarRespuestaCartaBorrosa
                    WinCard(onButtonClick = {
                        navController.navigate("home") {
                            popUpTo(Screen.ThirdGame.route) { inclusive = true }
                        }
                    }
                    )
                } else {
                    LoserCard(onButtonClick = {
                        navController.navigate("home") {
                            popUpTo(Screen.ThirdGame.route) { inclusive = true }
                        }
                    }
                    )
                }
                LaunchedEffect(Unit) {
                    if (verificarRespuestaHabilidadPokemon(
                            pokemonActual,
                            respuesta
                        )
                    ) {  //verificarRespuesta por verificarRespuestaCartaBorrosa
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
