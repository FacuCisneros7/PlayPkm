package com.electrofire.playpkm.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.Data.Repository.GameAttemptsRepository
import com.electrofire.playpkm.ui.Components.HomeStatsCard
import com.electrofire.playpkm.ui.Components.MyCardButton
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel

@Composable
fun HomeScreen(navController: NavController, statsViewModel: HomeStatsViewModel,authViewModel: AuthViewModel){

    val repo = GameAttemptsRepository() //Repo inicializado
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.signIn()
    }

    Box(Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "PLAYPKM", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp))

            Spacer(modifier = Modifier.height(16.dp))

            HomeStatsCard(statsViewModel= statsViewModel)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    MyCardButton(
                        title = "¿Quién es este Pokemon?",
                        imageRes = R.drawable.asfasfasfa,
                        onClick = {
                            repo.canPlayToday("first_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("first_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }

                item{
                    MyCardButton(
                        title = "Descubre la Carta Borrosa!",
                        imageRes = R.drawable.carta,
                        onClick = {
                            repo.canPlayToday("second_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("second_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }

                item{
                    MyCardButton(
                        title = "¿Tiene esta habilidad?",
                        imageRes = R.drawable.habilidad,
                        onClick = {
                            repo.canPlayToday("third_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("third_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }

                item{
                    MyCardButton(
                        title = "¿Conoces su potencial?",
                        imageRes = R.drawable.movimiento,
                        onClick = {
                            repo.canPlayToday("fourth_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("fourth_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "1 Fusion, 2 Pokemon",
                        imageRes = R.drawable.adasdss,
                        onClick = {
                            repo.canPlayToday("sixth_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("sixth_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }

                item {
                    MyCardButton(
                        title = "1 Silueta, 2 Pokemon",
                        imageRes = R.drawable.adasdad,
                        onClick = {
                            repo.canPlayToday("seventh_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("seventh_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }                        }
                    )
                }

                item{
                    MyCardButton(
                        title = "El juego imposible (Stats)",
                        imageRes = R.drawable.movimientodos,
                        onClick = {
                            repo.canPlayToday("fift_game") { canPlay ->
                                if(canPlay){
                                    navController.navigate("fift_game")
                                }
                                else{
                                    Toast.makeText(context,"Ya jugaste hoy! Espera a mañana", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }

            }
        }
    }

}