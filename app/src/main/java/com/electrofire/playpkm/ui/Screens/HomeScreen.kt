package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.Data.Repository.GameAttemptsRepository
import com.electrofire.playpkm.ui.Components.HomeStatsCard
import com.electrofire.playpkm.ui.Components.MyCardButton
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.ViewModels.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel

@Composable
fun HomeScreen(navController: NavController,
               statsViewModel: HomeStatsViewModel,
               authViewModel: AuthViewModel,
               homeContadorViewModel: HomeContadorViewModel = hiltViewModel()
){

    val timeLeft by homeContadorViewModel.timeLeft.collectAsState() // escucha el StateFlow

    val repo = GameAttemptsRepository() //Repo inicializado

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeContadorViewModel.startCountdown() // inicia el Flow
    }

    LaunchedEffect(Unit) {
        authViewModel.signIn()
    }

    Box(Modifier.fillMaxSize()){

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logohomecopia),
                contentDescription = null,
                modifier = Modifier.height(100.dp).wrapContentWidth()
            )

            Spacer(modifier = Modifier.height(17.dp))

            HomeStatsCard(statsViewModel= statsViewModel)

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.height(420.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    MyCardButton(
                        title = "¿Quién es este Pokemon?",
                        imageRes = R.drawable.asfasfasfa,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            navController.navigate("first_game")

                            /*
                            repo.canPlayTodayDos("first_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("first_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                             */


                        }
                    )
                }

                item{
                    MyCardButton(
                        title = "Descubre la Carta Borrosa!",
                        imageRes = R.drawable.carta,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            navController.navigate("second_game")

                            /*
                            repo.canPlayTodayDos("second_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("second_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                             */

                        }
                    )
                }

                item{
                    MyCardButton(
                        title = "¿Tiene esta habilidad?",
                        imageRes = R.drawable.habilidad,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
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

                item{
                    MyCardButton(
                        title = "¿Conoces su potencial?",
                        imageRes = R.drawable.movimiento,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
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

                item{
                    MyCardButton(
                        title = "El juego imposible (Stats)",
                        imageRes = R.drawable.movimientodos,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
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
                        title = "1 Fusion, 2 Pokemon",
                        imageRes = R.drawable.adasdss,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
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
                        title = "El mejor de nosotros",
                        imageRes = R.drawable.adasdad,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
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
                        title = "Impostor",
                        imageRes = R.drawable.dfsfsdf,
                        onClick = {
                            val mediaPlayer = MediaPlayer.create(context,R.raw.buttonuisoundeffect)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { it.release() }

                            navController.navigate("eight_game")

                            /*
                            repo.canPlayTodayDos("eight_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("eight_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                             */


                        }
                    )
                }

            }

            Spacer(modifier = Modifier.height(13.dp))

            Text(
                text = timeLeft,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.inversePrimary
            )

        }
    }

}