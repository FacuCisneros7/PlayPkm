package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import android.media.SoundPool
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.CardItems.SombrasInfCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.ContadorCorto
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.UserInputPokemon
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.ElevenGameState
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.SombrasInfViewModel

@Composable
fun ElevenGame(
    navController: NavController,
    viewModel: SombrasInfViewModel = hiltViewModel(),
    statsViewModel: HomeStatsViewModel
) {
    val usuario = statsViewModel.userData
    var respuesta by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val contador by viewModel.contador.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is ElevenGameState.Success && (state as ElevenGameState.Success).isGameOver) {
            statsViewModel.registrarMaxScoreElevenGame((state as ElevenGameState.Success).puntaje)
        }
    }

    val soundPool = remember {
        SoundPool.Builder().setMaxStreams(1).build()
    }
    val soundId = remember {
        soundPool.load(context, R.raw.buttonuisoundeffect, 1)
    }

    Box(
        Modifier.fillMaxSize()
    ) {

        GradientBackground()

        when (val currentState = state) {
            is ElevenGameState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading()
                }
            }

            is ElevenGameState.Error -> {
                if (currentState.message.contains("conexión", ignoreCase = true)) {
                    NotInternetScreen()
                } else {
                    ErrorScreen()
                }
            }

            is ElevenGameState.Success -> {
                val pokemonActual = currentState.pokemon
                val puntaje = currentState.puntaje
                val isGameOver = currentState.isGameOver

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!isGameOver) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.ninthgame_puntuation),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = " $puntaje", color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        ContadorCorto(contador = contador)

                        Spacer(Modifier.height(16.dp))

                        SombrasInfCard(pokemon = pokemonActual)

                        Spacer(modifier = Modifier.height(24.dp))

                        UserInputPokemon(
                            title = "Pokemon",
                            text = respuesta,
                            onTextChange = { respuesta = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                                viewModel.escribirPokemon(respuesta)
                                respuesta = ""
                            },
                            Modifier
                                .width(200.dp)
                                .height(40.dp),
                            elevation = ButtonDefaults.buttonElevation(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                disabledContentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.confirm),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Box {
                            Text(
                                text = "THOUSAND SHADOWS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    drawStyle = Stroke(width = 6f)
                                )
                            )
                            Text(
                                text = "THOUSAND SHADOWS",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 36.sp,
                                    lineHeight = 38.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(100.dp))

                            Box {
                                Text(
                                    text = stringResource(id = R.string.ninthgame_derrota),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 45.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 6f)
                                    )
                                )
                                Text(
                                    text = stringResource(id = R.string.ninthgame_derrota),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 45.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }

                            Spacer(Modifier.height(32.dp))

                            Row {
                                Box {
                                    Text(
                                        text = stringResource(id = R.string.ninthgame_puntuation_final),
                                        color = MaterialTheme.colorScheme.inversePrimary,
                                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = stringResource(id = R.string.ninthgame_puntuation_final),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontSize = 30.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            drawStyle = Stroke(width = 2f)
                                        )
                                    )
                                }
                                Box {
                                    Text(
                                        text = " $puntaje",
                                        color = MaterialTheme.colorScheme.outline,
                                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = " $puntaje",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontSize = 30.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            drawStyle = Stroke(width = 2f)
                                        )
                                    )
                                }

                            }

                            Spacer(Modifier.height(32.dp))

                            Row {
                                Box {
                                    Text(
                                        text = stringResource(id = R.string.ninthgame_max_puntuacion),
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = stringResource(id = R.string.ninthgame_max_puntuacion),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontSize = 30.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            drawStyle = Stroke(width = 2f)
                                        )
                                    )
                                }
                                Box {
                                    Text(
                                        text = " ${usuario.maxPointsDos}",
                                        color = MaterialTheme.colorScheme.outline,
                                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = " ${usuario.maxPointsDos}",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontSize = 30.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            drawStyle = Stroke(width = 2f)
                                        )
                                    )
                                }

                            }

                            Spacer(Modifier.height(32.dp))

                            ConfirmButton(
                                onConfirm = {
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }

                                    viewModel.iniciarJuego()
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp),
                                title = "jugar otra vez"
                            )

                            Spacer(Modifier.height(64.dp))

                            Button(
                                onClick = {
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }

                                    navController.navigate("home") {
                                        popUpTo(Screen.ElevenGame.route) { inclusive = true }
                                    }
                                },
                                elevation = ButtonDefaults.buttonElevation(5.dp),
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.ninthgame_home),
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}