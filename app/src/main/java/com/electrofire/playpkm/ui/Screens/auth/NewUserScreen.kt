package com.electrofire.playpkm.ui.Screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.ChoiseImage
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.NationalityDropdown
import com.electrofire.playpkm.ui.Components.PokemonWithFlag
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@Composable
fun NewUserScreen(navController: NavController, statsViewModel: HomeStatsViewModel) {
    var userName by remember { mutableStateOf("") }
    var respondido by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedAvatarId by remember { mutableStateOf<String?>(null) }
    var selectedNationality by remember { mutableStateOf<String?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(respondido) {
        if (respondido) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.NewUserScreen.route) { inclusive = true }
            }
        }
    }

    BackHandler(enabled = true) {
        // No hace nada → el botón atrás queda bloqueado
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Dialog(onDismissRequest = { /* Bloqueado */ }) {
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(durationMillis = 200)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 200)
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.85f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.95f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 24.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CREAR PERFIL",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 28.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            placeholder = {
                                Text(
                                    text = "Tu nombre de usuario",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(
                                    alpha = 0.5f
                                ),
                                unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(
                                    alpha = 0.5f
                                ),
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                cursorColor = MaterialTheme.colorScheme.tertiary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
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
                            painter = selectedAvatarId ?: R.drawable.pokeball,
                            nationality = selectedNationality,
                            imageSize = 110.dp,
                            borderColor = MaterialTheme.colorScheme.tertiary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ChoiseImage(selectedAvatarId = selectedAvatarId ?: "0025") { id ->
                            selectedAvatarId = id
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
                                normalized.isBlank() -> errorMessage =
                                    "El nombre no puede estar vacío"

                                normalized.length >= 10 -> errorMessage =
                                    "Debe tener menos de 10 caracteres"

                                selectedNationality == null -> errorMessage =
                                    "Debes elegir una nacionalidad"

                                selectedAvatarId == null -> errorMessage = "Debes elegir un Pokémon"
                                else -> {
                                    selectedAvatarId?.let {
                                        statsViewModel.seleccionarAvatar(it)
                                        statsViewModel.registrarUserName(normalized)
                                        selectedNationality?.let { nat ->
                                            statsViewModel.registrarNationality(
                                                nat
                                            )
                                        }
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
}
