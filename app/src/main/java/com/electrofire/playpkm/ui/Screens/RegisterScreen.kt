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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.ConfirmButtonRegLog
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.ViewModels.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var respondido by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }


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
                    .padding(top = 64.dp, start = 32.dp, end = 32.dp),
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
                    // Contorno
                    Text(
                        text = "REGISTRO",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    // Relleno
                    Text(
                        text = "REGISTRO",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .fillMaxSize(),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                ) {
                    TextField(
                        value = email,
                        textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                        onValueChange = {
                            email = it
                            if (email.length < 30) {
                                errorMessage = null
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Email",
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

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .fillMaxSize(),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                ) {
                    TextField(
                        value = password,
                        textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                        onValueChange = {
                            password = it
                            if (password.length >= 6) {
                                errorMessage = null
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Contraseña",
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
                        visualTransformation =
                            if (passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .fillMaxSize(),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                ) {
                    TextField(
                        value = repeatPassword,
                        textStyle = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                        onValueChange = {
                            repeatPassword = it
                            if (repeatPassword.length >= 6) {
                                errorMessage = null
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Repetir contraseña",
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
                        visualTransformation =
                            if (repeatPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        trailingIcon = {
                            val image =
                                if (repeatPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = {
                                repeatPasswordVisible = !repeatPasswordVisible
                            }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        },
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

                Spacer(modifier = Modifier.height(32.dp))

                ConfirmButton(
                    onConfirm = {
                        if (email.isBlank()) {
                            errorMessage = "El email no puede estar vacío"
                        } else if (password.length <= 6) {
                            errorMessage = "La contraseña debe tener por lo menos 7 caracteres"
                        } else if (password != repeatPassword) {
                            errorMessage = "Verifique que coincidan ambas contraseñas"
                        } else {
                            authViewModel.register(
                                email = email,
                                password = password,
                                onSuccess = { uid ->
                                    respondido = true
                                },
                                onError = { error ->
                                    errorMessage = error
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(64.dp))

                ConfirmButtonRegLog(
                    onConfirm = {
                        navController.navigate("login")
                    },
                    title = "iniciar sesion"
                )

            }
        }

    } else {
        navController.navigate("new_user") {
            popUpTo("register") { inclusive = true }
        }
    }
}

