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
import com.electrofire.playpkm.ui.CardItems.FusionCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.PokemonesFusionCard
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.UserInputPokemonDos
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModel
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModelDos
import com.electrofire.playpkm.ui.ViewModels.FusionViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaFusion

@Composable
fun SixthGame(navController: NavController, viewModel1: AutoPokeViewModel = hiltViewModel(),
              viewModel2: AutoPokeViewModelDos = hiltViewModel(), viewModel: FusionViewModel = hiltViewModel(),
              statsViewModel: HomeStatsViewModel)
{
    var respuesta by remember { mutableStateOf("") }
    var respuestaDos by remember { mutableStateOf("") }
    val fusionActual = viewModel.fusion
    var respondido by remember { mutableStateOf(false) }

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
                    painter = painterResource(id = R.drawable.sextojuego),
                    contentDescription = null,
                    modifier = Modifier.height(80.dp).wrapContentWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                FusionCard()

                Spacer(modifier = Modifier.height(32.dp))

                UserInputPokemon(
                    title = "Pokemon 1",
                    text = respuesta,
                    onTextChange = { respuesta = it },
                    viewModel = viewModel1
                    )

                Spacer(modifier = Modifier.height(24.dp))

                UserInputPokemonDos(
                    title = "Pokemon 2",
                    text = respuestaDos,
                    onTextChange = { respuestaDos = it },
                    viewModel = viewModel2)

                Spacer(modifier = Modifier.height(48.dp))

                ConfirmButton(onConfirm = { respondido = true })
            } else{
                Spacer(modifier = Modifier.height(32.dp))

                FusionCard()

                Spacer(modifier = Modifier.height(32.dp))

                PokemonesFusionCard()

                Spacer(modifier = Modifier.height(48.dp))

                if (verificarRespuestaFusion(fusionActual, respuesta, respuestaDos)) {
                    WinCard(onButtonClick = { navController.navigate("home") })
                } else {
                    LoserCard(onButtonClick = { navController.navigate("home") })
                }
                LaunchedEffect(Unit) {
                    if (verificarRespuestaFusion(fusionActual, respuesta, respuestaDos)) {
                        statsViewModel.registrarVictoria()
                    } else {
                        statsViewModel.registrarDerrota()
                    }
                }
            }
        }

    }
}