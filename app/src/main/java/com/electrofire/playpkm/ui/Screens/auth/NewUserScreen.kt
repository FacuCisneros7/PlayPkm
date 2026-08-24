package com.electrofire.playpkm.ui.Screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.ChoiseImage
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.NationalityDropdown
import com.electrofire.playpkm.ui.Components.PokemonWithFlag
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@Composable
fun NewUserScreen(navController: NavController, statsViewModel: HomeStatsViewModel) {
    var userName by remember { mutableStateOf("") }
    var respondido by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedImage by remember { mutableStateOf<String?>(null) }
    var selectedNationality by remember { mutableStateOf<String?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        isVisible = true
    }

    androidx.compose.runtime.LaunchedEffect(statsViewModel.isUserLoaded) {
        if (!statsViewModel.isUserLoaded) {
            statsViewModel.cargarStats()
        }
    }

    BackHandler(enabled = true) {
        // No hace nada → el botón atrás queda bloqueado
    }

    if (!respondido) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Dialog(onDismissRequest = { /* Bloqueado */ }) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.8f, animationSpec = tween(500))
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                        elevation = CardDefaults.cardElevation(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 24.dp, bottom = 24.dp, start = 8.dp, end = 8.dp)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box {
                                Text(
                                    text = "REGISTRO ENTRENADOR",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 22.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 4f)
                                    )
                                )
                                Text(
                                    text = "REGISTRO ENTRENADOR",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 22.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = userName,
                                onValueChange = {
                                    userName = it
                                    val normalized = it.trim().replace("\\s+".toRegex(), " ")
                                    if (normalized.length < 10) {
                                        errorMessage = null
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Nombre de usuario",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyLarge,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                    focusedTextColor = MaterialTheme.colorScheme.primary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                                    cursorColor = MaterialTheme.colorScheme.tertiary
                                ),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            NationalityDropdown(
                                selectedNationality = selectedNationality,
                                onNationalitySelected = { selectedNationality = it },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Previsualización del Avatar seleccionado
                            PokemonWithFlag(
                                painter = selectedImage ?: R.drawable.pokeball,
                                nationality = selectedNationality,
                                imageSize = 110.dp,
                                borderColor = MaterialTheme.colorScheme.tertiary
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            ChoiseImage(selectedImage = selectedImage) { url ->
                                selectedImage = url
                            }

                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            ConfirmButton(onConfirm = {
                                val normalized = userName.trim().replace("\\s+".toRegex(), " ")
                                when {
                                    normalized.isBlank() -> errorMessage = "El nombre no puede estar vacío"
                                    normalized.length >= 10 -> errorMessage =
                                        "Debe tener menos de 10 caracteres"

                                    selectedNationality == null -> errorMessage = "Debes elegir una nacionalidad"
                                    selectedImage == null -> errorMessage = "Debes elegir un Pokémon"
                                    else -> {
                                        selectedImage?.let { 
                                            statsViewModel.registrarFoto(it)
                                            statsViewModel.registrarUserName(normalized)
                                            selectedNationality?.let { nat -> statsViewModel.registrarNationality(nat) }
                                            respondido = true
                                        }
                                    }
                                }
                            }
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    Text(
                        text = "¡BIENVENIDO!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    Text(
                        text = "¡BIENVENIDO!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }

                Spacer(Modifier.height(32.dp))

                PokemonWithFlag(
                    painter = selectedImage,
                    nationality = selectedNationality,
                    imageSize = 150.dp,
                    borderColor = MaterialTheme.colorScheme.tertiary
                )

                Spacer(Modifier.height(24.dp))

                Box {
                    Text(
                        text = userName.uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 4f)
                        )
                    )
                    Text(
                        text = userName.uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                Spacer(Modifier.height(48.dp))

                Button(
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("new_user") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(45.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "CONTINUAR",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}
