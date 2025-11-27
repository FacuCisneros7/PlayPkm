package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import com.electrofire.playpkm.ui.CardItems.PokemonCard
import com.electrofire.playpkm.ui.CardItems.SilhouettePokemonCard
import com.electrofire.playpkm.ui.Components.BannerAdd
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.Contador
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModel
import com.electrofire.playpkm.ui.ViewModels.ContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.PokemonViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaPokemon

@Composable
fun FirstGame(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel(),
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

    Box(
        Modifier.fillMaxSize()
    ) {

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!respondido) {

                Box {
                    Text(
                        text = "EASY GAME",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            lineHeight = 38.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    Text(
                        text = "EASY GAME",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            lineHeight = 38.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SilhouettePokemonCard()

                Spacer(modifier = Modifier.height(24.dp))

                UserInputPokemon(
                    title = "Pokemon",
                    text = respuesta,
                    onTextChange = { respuesta = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ConfirmButton(onConfirm = { respondido = true })

                Spacer(modifier = Modifier.height(32.dp))

                Contador(contadorViewModel = contadorViewModel)
            } else {

                Spacer(modifier = Modifier.height(90.dp))

                PokemonCard()

                Spacer(modifier = Modifier.height(64.dp))

                if (verificarRespuestaPokemon(pokemonActual, respuesta)) {
                    WinCard(onButtonClick = {
                        navController.navigate("home") {
                            popUpTo(Screen.FirstGame.route) { inclusive = true }
                        }
                    }
                    )
                } else {
                    LoserCard(onButtonClick = {
                        navController.navigate("home") {
                            popUpTo(Screen.FirstGame.route) { inclusive = true }
                        }
                    }
                    )
                }

                LaunchedEffect(Unit) {
                    if (verificarRespuestaPokemon(pokemonActual, respuesta)) {
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