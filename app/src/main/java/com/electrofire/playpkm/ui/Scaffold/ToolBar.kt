package com.electrofire.playpkm.ui.Scaffold

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Themes.PLAYPKMTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar() {
    val context = LocalContext.current
    var showInfoCard by remember { mutableStateOf(false) }

        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Playpkm",
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.headlineLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = { showInfoCard = true }) {
                    Image(
                        painter = painterResource(id = R.drawable.comentarioinfo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(30.dp).alpha(0.5f)
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
                        modifier = Modifier.size(30.dp).alpha(0.5f)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        if (showInfoCard) {
            Dialog(onDismissRequest = { showInfoCard = false }) {
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
                        modifier = Modifier.fillMaxSize().padding(12.dp)
                    ) {

                        item {
                            Text(
                                text = "ACERCA DE PLAYPKM",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 17.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "PlayPkm es una aplicacion movil diseñada con la intención de " +
                                        "desafiar tu conocimiento pokemon, a su vez te permitirá medirte " +
                                        "en un ranking global con el resto de los usuarios!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "MINIJUEGOS",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Desafia los 7 minijuegos todos los dias para llegar al top del ranking! " +
                                        "Los minijuegos se reinician a diario, a las 00:00 UTC (Guiarse con el contador en pantalla)",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "EASY GAME",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Para completar el juegos mas sencillo, deberas adivinar la silueta del pokemon a contrarreloj!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "BLURRED CARD",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Consigue adivinar el pokemon en la carta borrosa!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "ONE ABILITY",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Podras recordar una habilidad del pokemon del día?",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "POWER OF MOVE",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Adivina la potencia del movimiento! No es tan fácil como parece...",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "EX HARD GAME",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Un juego verdaderamente dificil! Por eso mismo contarás " +
                                        "con 3 vidas para adivinar el pokemon detrás de esas stats.",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "1,2,3... FUSION!",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Deberás demostrar que puedes reconocer a los 2 Pokemon fusionados!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "THE BEST",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Habrá 3 Pokemon en pantalla y se mencionará una stat... " +
                                        "Cual de los 3 Pokemon tiene más puntos base de dicha stat?",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Text(
                                text = "Impostor",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Visualizarás 1 habilidad, y 5 pokemon... " +
                                        "Encuentra al único que no posee dicha habilidad!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(20.dp))
                        }

                        item {
                            Text(
                                text = "GRACIAS POR DESCARGAR PLAYPKM!",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Toda sugerencia, " +
                                        "aviso de errores, o ideas para el futuro son recibidas con gratitud. (Mi Instagram se encuentra " +
                                        "arriba a la derecha)",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                    }

                }
            }
        }
}

