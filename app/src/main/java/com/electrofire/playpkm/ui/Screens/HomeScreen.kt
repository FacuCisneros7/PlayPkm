package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.Data.Repository.GameAttemptsRepository
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.BannerAdd
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.HomeStatsCard
import com.electrofire.playpkm.ui.Components.MyCardButton
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    statsViewModel: HomeStatsViewModel,
    authViewModel: AuthViewModel,
    homeContadorViewModel: HomeContadorViewModel = hiltViewModel()
) {

    val timeLeft by homeContadorViewModel.timeLeft.collectAsState() // escucha el StateFlow

    val repo = GameAttemptsRepository() //Repo inicializado

    val context = LocalContext.current

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
        homeContadorViewModel.startCountdown() // inicia el Flow
    }

    LaunchedEffect(Unit) {
        authViewModel.checkUserLoggedIn()
    }

    Box(Modifier.fillMaxSize()) {

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logohomecopia),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .wrapContentWidth()
            )

            Spacer(modifier = Modifier.height(17.dp))

            HomeStatsCard(statsViewModel = statsViewModel)

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.height(370.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    MyCardButton(
                        title = "EASY GAME",
                        imageRes = R.drawable.asfasfasfa,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }
                            navController.navigate("first_game")
//                            repo.canPlayTodayDos("first_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("first_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }

                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "BLURRED CARD",
                        imageRes = R.drawable.carta,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            navController.navigate("second_game")
//                            repo.canPlayTodayDos("second_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("second_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }

                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "ONE ABILITY",
                        imageRes = R.drawable.habilidad,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }
                            navController.navigate("third_game")
//                            repo.canPlayTodayDos("third_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("third_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }
                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "POWER OF MOVE",
                        imageRes = R.drawable.movimiento,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }
                            navController.navigate("fourth_game")
//                            repo.canPlayTodayDos("fourth_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("fourth_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }
                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "MYSTERIOUS STATS",
                        imageRes = R.drawable.movimientodos,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }
                            navController.navigate("fift_game")
//                            repo.canPlayTodayDos("fift_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("fift_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }
                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "FUSION!",
                        imageRes = R.drawable.fision,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }
                            navController.navigate("sixth_game")
//                            repo.canPlayTodayDos("sixth_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("sixth_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }
                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "THE BEST",
                        imageRes = R.drawable.adasdad,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }
                            navController.navigate("seventh_game")
//                            repo.canPlayTodayDos("seventh_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("seventh_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }

                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "IMPOSTOR",
                        imageRes = R.drawable.dfsfsdf,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }
                            navController.navigate("eight_game")
//                            repo.canPlayTodayDos("eight_game") { canPlay ->
//                                if(canPlay){
//                                    navController.navigate("eight_game")
//                                }
//                                else{
//                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
//                                }
//                            }

                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "GOOD CHOISE",
                        imageRes = R.drawable.goodchoisess,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            navController.navigate(Screen.NinthGame.route)
                        }
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "Actualización en: ",
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
        BannerAdd(Modifier.align(alignment = Alignment.BottomStart))
    }

}