package com.electrofire.playpkm.ui.Scaffold

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.MyCardButton
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar(navController: NavController, statsViewModel: HomeStatsViewModel) {
    val context = LocalContext.current
    var showInfoCard by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Playpkm",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = { showInfoCard = true }) {
                Image(
                    painter = painterResource(id = R.drawable.comentarioinfo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/pokememesespanol/")
                )
                context.startActivity(intent)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.instagram),
                    contentDescription = "Logo",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )

    if (showInfoCard) {
        Dialog(onDismissRequest = { showInfoCard = false }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.7f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(3.dp, Color.Black),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.inversePrimary
                    )
                ) {

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {

                        item {
                            Box {
                                Text(
                                    text = "ACERCA DE PLAYPKM",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 2f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "ACERCA DE PLAYPKM",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }

                        item {

                            Text(
                                text = "PlayPkm es una aplicacion movil diseñada con la intención de " +
                                        "desafiar tu conocimiento pokemon, a su vez te permitirá medirte " +
                                        "en un ranking global con el resto de los usuarios!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Box {
                                Text(
                                    text = "MINIJUEGOS",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "MINIJUEGOS",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Desafia los 9 minijuegos todos los dias para llegar al top del ranking! " +
                                        "Los minijuegos se reinician a diario, a las 00:00 UTC (Guiarse con el contador en pantalla)",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "EASY GAME",
                                imageRes = R.drawable.asfasfasfa,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Para completar el juegos mas sencillo, deberas adivinar la silueta del pokemon a contrarreloj!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "BLURRED CARD",
                                imageRes = R.drawable.carta,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Consigue adivinar el pokemon en la carta borrosa!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "ONE ABILITY",
                                imageRes = R.drawable.habilidad,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Podras recordar una habilidad del pokemon del día?",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "POWER OF MOVE",
                                imageRes = R.drawable.movimiento,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Adivina la potencia del movimiento! No es tan fácil como parece...",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "MYSTERIOUS STATS",
                                imageRes = R.drawable.movimientodos,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Un juego verdaderamente dificil! Por eso mismo contarás " +
                                        "con 3 vidas para adivinar el pokemon detrás de esas stats.",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "FUSION!",
                                imageRes = R.drawable.fision,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Deberás demostrar que puedes reconocer a los 2 Pokemon fusionados!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "THE BEST",
                                imageRes = R.drawable.adasdad,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))
                        }

                        item {
                            Text(
                                text = "Habrá 3 Pokemon en pantalla y se mencionará una stat... " +
                                        "Cual de los 3 Pokemon tiene más puntos base de dicha stat?",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = "IMPOSTOR",
                                imageRes = R.drawable.dfsfsdf,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Visualizarás 1 habilidad, y 5 pokemon... " +
                                        "Encuentra al único que no posee dicha habilidad!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(24.dp))
                        }

                        item {
                            MyCardButton(
                                title = "GOOD CHOICE",
                                imageRes = R.drawable.goodchoisesc,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = "Este minijuego se podrá jugar de manera ILIMITADA! " +
                                        "Selecciona el pokemon mas fuerte y suma tu mayor puntuación!" +
                                        " Además, ten en cuenta que no sumará puntos para el ranking global...",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(24.dp))
                        }

                        item {
                            Box {
                                Text(
                                    text = "SUGERENCIA",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "SUGERENCIA",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                        }

                        item {
                            Text(
                                text = "Para tener una mejor experiencia se recomienda ajustar el tamaño de pantalla de mitad para abajo",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        item {
                            Image(
                                painter = painterResource(id = R.drawable.pantallarecomendable),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(175.dp)
                                    .wrapContentHeight()
                            )
                            Spacer(Modifier.height(32.dp))
                        }

                        item {
                            Box {
                                Text(
                                    text = "GRACIAS POR DESCARGAR PLAYPKM!",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "GRACIAS POR DESCARGAR PLAYPKM!",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Toda sugerencia, " +
                                        "aviso de errores, o ideas para el futuro son recibidas con gratitud. (Mi Instagram se encuentra " +
                                        "arriba a la derecha)",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(32.dp))
                        }

                        item {
                            Text(
                                text = "Creado por Facundo Cisneros",
                                color = MaterialTheme.colorScheme.onSecondary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                    }

                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        showInfoCard = false
                        statsViewModel.reset()
                        navController.navigate("register") {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                        Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier
                        .width(180.dp)
                        .height(40.dp),
                    elevation = ButtonDefaults.buttonElevation(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(
                        text = "Cerrar sesión",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
    }
}

