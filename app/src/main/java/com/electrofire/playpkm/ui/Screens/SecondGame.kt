package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.ui.CardItems.BlurredCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.CardItems.RealCard
import com.electrofire.playpkm.ui.Components.Contador
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.ViewModels.CartaViewModel
import com.electrofire.playpkm.ui.ViewModels.ContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaCartaBorrosa

@Composable
fun SecondGame(navController: NavController, viewModel: CartaViewModel = hiltViewModel(),
               statsViewModel: HomeStatsViewModel,
               contadorViewModel: ContadorViewModel = viewModel())
{
    var respuesta by remember { mutableStateOf("") }
    val cartaActual = viewModel.carta
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!respondido){
            Text(
                text = "¿Que pokemon está en la carta?",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            BlurredCard()

            Spacer(modifier = Modifier.height(16.dp))

            UserInputPokemon(title = "Pokemon",text = respuesta, onTextChange = { respuesta = it })

            Spacer(modifier = Modifier.height(16.dp))

            ConfirmButton( onConfirm = { respondido = true })

            Spacer(modifier = Modifier.height(90.dp))

            Contador(contadorViewModel = contadorViewModel)
            }
            else{
                Text(
                    text = "La respuesta correcta es...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                RealCard()

                Spacer(modifier = Modifier.height(32.dp))

                if (verificarRespuestaCartaBorrosa(cartaActual, respuesta)) {  //verificarRespuesta por verificarRespuestaCartaBorrosa
                    WinCard(onButtonClick = { navController.navigate("home") })
                } else {
                    LoserCard(onButtonClick = { navController.navigate("home") })
                }
                LaunchedEffect(Unit) {
                    if (verificarRespuestaCartaBorrosa(cartaActual, respuesta)) {  //verificarRespuesta por verificarRespuestaCartaBorrosa
                        statsViewModel.registrarVictoria()
                    } else {
                        statsViewModel.registrarDerrota()
                    }
                }
            }
        }
    }

}
