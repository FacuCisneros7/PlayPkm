package com.electrofire.playpkm.ui.Screens.main

import android.app.Activity
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.AdReward
import com.electrofire.playpkm.ui.Components.GifAnimation
import com.electrofire.playpkm.ui.Components.HomeStatsCard
import com.electrofire.playpkm.ui.Components.MyCardButton
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.main.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    statsViewModel: HomeStatsViewModel,
    authViewModel: AuthViewModel,
    homeContadorViewModel: HomeContadorViewModel = hiltViewModel()
) {

    val timeLeft by homeContadorViewModel.timeLeft.collectAsState() // escucha el StateFlow

    val context = LocalContext.current

    val activity = context as Activity

    val rewardedAdManager = remember { AdReward(context) }

    val listState = rememberLazyListState()

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 && lastVisibleItem == totalItems - 1
        }
    }

    BackHandler(enabled = true) {
        // No hace nada → el botón atrás queda bloqueado
    }

    val color = remember(timeLeft) {
        val hours = timeLeft.split(":").firstOrNull()?.toIntOrNull() ?: 0
        when {
            hours < 1 -> Color.Red
            hours < 3 -> Color(0xFFFF9800) // Naranja
            hours < 10 -> Color(0xFF4CAF50) // Verde
            else -> Color(0xFFBABECF)
        }
    }

    LaunchedEffect(Unit) {
        rewardedAdManager.loadAd()
    }

    LaunchedEffect(Unit) {
        homeContadorViewModel.startCountdown() // inicia el Flow
    }

    LaunchedEffect(Unit) {
        authViewModel.checkUserLoggedIn()
    }

    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logohomecopia),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .wrapContentWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            HomeStatsCard(statsViewModel = statsViewModel)

            Spacer(modifier = Modifier.height(24.dp))

            Box {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.height(380.dp),
                    contentPadding = PaddingValues(top = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {
                        MyCardButton(
                            title = "BEFORE OR AFTER",
                            imageRes = R.drawable.beforeorafter,
                            showBadge = true,
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                navController.navigate(Screen.BeforeAfterGame.route)
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "THOUSAND SHADOWS",
                            imageRes = R.drawable.thousandshadows,
                            showBadge = true,
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                navController.navigate(Screen.ThousandShadowsGame.route)
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "GOOD CHOICE",
                            imageRes = R.drawable.goodchoisenew,
                            showBadge = true,
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                navController.navigate(Screen.GoodChoiceGame.route)
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "TYPES",
                            imageRes = R.drawable.typegame,
                            showBadge = statsViewModel.canPlayGame("thirteen_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("thirteen_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("thirteen_game")
                                        navController.navigate("type_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "ITEM MYSTERY",
                            imageRes = R.drawable.itemgame,
                            showBadge = statsViewModel.canPlayGame("forteen_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("forteen_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("forteen_game")
                                        navController.navigate(Screen.ItemMysteryGame.route)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "ZOOM GAME",
                            imageRes = R.drawable.adasdss,
                            showBadge = statsViewModel.canPlayGame("ten_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("ten_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("ten_game")
                                        navController.navigate("ten_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "EASY GAME",
                            imageRes = R.drawable.asfasfasfa,
                            showBadge = statsViewModel.canPlayGame("first_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("first_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("first_game")
                                        navController.navigate("first_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "BLURRED CARD",
                            imageRes = R.drawable.carta,
                            showBadge = statsViewModel.canPlayGame("second_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("second_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("second_game")
                                        navController.navigate("second_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "ONE ABILITY",
                            imageRes = R.drawable.habilidad,
                            showBadge = statsViewModel.canPlayGame("third_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("third_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("third_game")
                                        navController.navigate("third_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "POWER OF MOVE",
                            imageRes = R.drawable.movimiento,
                            showBadge = statsViewModel.canPlayGame("fourth_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("fourth_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("fourth_game")
                                        navController.navigate("fourth_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "MYSTERIOUS STATS",
                            imageRes = R.drawable.movimientodos,
                            showBadge = statsViewModel.canPlayGame("fift_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }
                                statsViewModel.verificarAccesoJuego("fift_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("fift_game")
                                        navController.navigate("fift_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "FUSION!",
                            imageRes = R.drawable.fision,
                            showBadge = statsViewModel.canPlayGame("sixth_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("sixth_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("sixth_game")
                                        navController.navigate("sixth_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "THE BEST",
                            imageRes = R.drawable.adasdad,
                            showBadge = statsViewModel.canPlayGame("seventh_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }
                                statsViewModel.verificarAccesoJuego("seventh_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("seventh_game")
                                        navController.navigate("seventh_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        )
                    }

                    item {
                        MyCardButton(
                            title = "IMPOSTOR",
                            imageRes = R.drawable.dfsfsdf,
                            showBadge = statsViewModel.canPlayGame("eight_game"),
                            onClick = {
                                val mediaPlayer =
                                    MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener { it.release() }

                                statsViewModel.verificarAccesoJuego("eight_game") { canPlay ->
                                    if (canPlay) {
                                        statsViewModel.registrarIntentoJuego("eight_game")
                                        navController.navigate("eight_game")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ya jugaste hoy! Espera a mañana",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        )
                    }

                }

                if (!isAtBottom) {
                    GifAnimation(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = stringResource(id = R.string.actualizacion_en) + " ",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.inversePrimary
                )
                Text(
                    text = timeLeft,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 15.sp),
                    color = color
                )
            }

        }
    }

}