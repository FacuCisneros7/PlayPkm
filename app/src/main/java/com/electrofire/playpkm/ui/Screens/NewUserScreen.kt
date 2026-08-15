package com.electrofire.playpkm.ui.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel

@Composable
fun NewUserScreen(navController: NavController, statsViewModel: HomeStatsViewModel) {
    var userName by remember { mutableStateOf("") }
    var respondido by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedImage by remember { mutableStateOf<String?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        isVisible = true
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

                            Card(
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(55.dp),
                                elevation = CardDefaults.cardElevation(0.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                ),
                                border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary)
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
                                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp)
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                        focusedTextColor = MaterialTheme.colorScheme.primary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                                        focusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        cursorColor = MaterialTheme.colorScheme.tertiary,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Previsualización del Avatar seleccionado
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        shape = CircleShape
                                    )
                                    .padding(4.dp)
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedImage != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(selectedImage),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.pokeball),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        alpha = 0.5f
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            ChoiseImage(selectedImage = selectedImage) { url ->
                                selectedImage = url
                            }

                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage!!,
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

                                    selectedImage == null -> errorMessage = "Debes elegir un Pokémon"
                                    else -> {
                                        statsViewModel.registrarFoto(selectedImage!!)
                                        statsViewModel.registrarUserName(normalized)
                                        respondido = true
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

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .border(4.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                        .padding(6.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImage),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

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
