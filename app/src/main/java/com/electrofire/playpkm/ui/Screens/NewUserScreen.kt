package com.electrofire.playpkm.ui.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.ChoiseImage
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import kotlinx.coroutines.delay

@Composable
fun NewUserScreen(navController: NavController, statsViewModel: HomeStatsViewModel) {
    var userName by remember { mutableStateOf("") }
    var respondido by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var imageSelected by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        // No hace nada → el botón atrás queda bloqueado
    }

    if (!respondido) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            GradientBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp, start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logohomecopia),
                    contentDescription = null,
                    modifier = Modifier
                        .height(120.dp)
                        .wrapContentWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box {
                    Text(
                        text = "ELIGE TU POKEMON INSIGNIA!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 2f)
                        )
                    )
                    Text(
                        text = "ELIGE TU POKEMON INSIGNIA!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                ChoiseImage(statsViewModel) { selected ->
                    imageSelected = selected
                }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .width(200.dp)
                        .height(55.dp)
                        .fillMaxSize(),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                ) {
                    TextField(
                        value = userName,
                        textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                        onValueChange = {
                            userName = it
                            val normalized = it.trim().replace("\\s+".toRegex(), " ")
                            if (normalized.length < 10) {
                                errorMessage = null
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Nombre de usuario",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                                textAlign = TextAlign.Center
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.onSecondary,    // Fondo cuando está enfocado
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,  // Fondo cuando NO está enfocado
                            focusedTextColor = Color.White,       // Texto ingresado
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.inversePrimary, // Placeholder enfocado
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.inverseSurface, // Placeholder desenfocado
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(64.dp))

                ConfirmButton(onConfirm = {
                    val normalized = userName.trim().replace("\\s+".toRegex(), " ")
                    when {
                        normalized.isBlank() -> errorMessage = "El nombre no puede estar vacío"
                        normalized.length >= 10 -> errorMessage =
                            "Debe tener menos de 10 caracteres"

                        !imageSelected -> errorMessage = "Debes elegir una imagen"
                        else -> {
                            statsViewModel.registrarUserName(normalized)
                            respondido = true
                        }
                    }
                }
                )

            }
        }
    } else {
        LaunchedEffect(Unit) {
            delay(3000)
            navController.navigate("home") {
                popUpTo("new_user") { inclusive = true }
            }
        }
    }
}