package com.electrofire.playpkm.ui.Screens.main

import android.app.Activity
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.AdReward
import com.electrofire.playpkm.ui.Components.GifAnimation
import com.electrofire.playpkm.ui.Components.MyCardButton
import com.electrofire.playpkm.ui.Components.WeeklyRewardsDialog
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

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Desafíos Diarios, 1: Juega cuando quieras

    val dailyGameIds = listOf(
        "first_game", "second_game", "third_game",
        "sixth_game", "seventh_game", "fift_game", "eight_game",
        "ten_game", "thirteen_game", "forteen_game", "fifteen_game",
        "sixteen_game", "seventeen_game", "eighteen_game"
    )
    val totalDaily = dailyGameIds.size
    val completedCount = dailyGameIds.count { gameId ->
        !statsViewModel.canPlayGame(gameId)
    }

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

    statsViewModel.weeklyRewardToShow?.let { rewardData ->
        WeeklyRewardsDialog(
            rewardData = rewardData,
            onDismiss = { statsViewModel.dismissWeeklyReward() }
        )
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                TabButton(
                    text = "RETOS DIARIOS",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                TabButton(
                    text = "RETOS LIBRES",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Header con el contador o sin límite
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = if (selectedTab == 0) "$completedCount/$totalDaily completados" else "∞ SIN LÍMITE",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.height(380.dp),
                    contentPadding = PaddingValues(top = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedTab == 1) {
                        // Juegos Repetitivos (Juega cuando quieras)
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
                    } else {
                        // Juegos Diarios
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
                                title = "PYRAMID POKEMON",
                                imageRes = R.drawable.pyramid,
                                showBadge = statsViewModel.canPlayGame("fifteen_game"),
                                onClick = {
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }

                                    statsViewModel.verificarAccesoJuego("fifteen_game") { canPlay ->
                                        if (canPlay) {
                                            statsViewModel.registrarIntentoJuego("fifteen_game")
                                            navController.navigate(Screen.PyramidGame.route)
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
                                title = "POKE HEAVY",
                                imageRes = R.drawable.pokeheavy,
                                showBadge = statsViewModel.canPlayGame("seventeen_game"),
                                onClick = {
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }
                                    statsViewModel.verificarAccesoJuego("seventeen_game") { canPlay ->
                                        if (canPlay) {
                                            statsViewModel.registrarIntentoJuego("seventeen_game")
                                            navController.navigate(Screen.WeightGame.route)
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
                                title = "POKE SPEED",
                                imageRes = R.drawable.pokespeed,
                                showBadge = statsViewModel.canPlayGame("eighteen_game"),
                                onClick = {
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }

                                    statsViewModel.verificarAccesoJuego("eighteen_game") { canPlay ->
                                        if (canPlay) {
                                            statsViewModel.registrarIntentoJuego("eighteen_game")
                                            navController.navigate(Screen.SpeedGame.route)
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
                                title = "POKEMON POLYGLOT",
                                imageRes = R.drawable.lenguajes,
                                showBadge = statsViewModel.canPlayGame("sixteen_game"),
                                onClick = {
                                    val mediaPlayer =
                                        MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }
                                    statsViewModel.verificarAccesoJuego("sixteen_game") { canPlay ->
                                        if (canPlay) {
                                            statsViewModel.registrarIntentoJuego("sixteen_game")
                                            navController.navigate(Screen.LanguageGame.route)
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
                                imageRes = R.drawable.zoom,
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
                                imageRes = R.drawable.easygame,
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
                                imageRes = R.drawable.card,
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
                                imageRes = R.drawable.ability,
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
                                title = "MYSTERIOUS STATS",
                                imageRes = R.drawable.stats,
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
                                imageRes = R.drawable.fusion,
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
                                imageRes = R.drawable.thebest,
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
                                imageRes = R.drawable.impostor,
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

@Composable
fun TabButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            2.dp,
            if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
                alpha = 0.5f
            )
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.5f
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.5f
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
