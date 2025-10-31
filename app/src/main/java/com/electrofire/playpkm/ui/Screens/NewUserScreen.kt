package com.electrofire.playpkm.ui.Screens

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.ChoiseImage
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.ViewModels.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel

@Composable
fun NewUserScreen(navController: NavController, statsViewModel: HomeStatsViewModel) {
    var userName by remember { mutableStateOf("") }
    var respondido by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (!respondido) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            GradientBackground()

            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    painter = painterResource(id = R.drawable.logohomecopia),
                    contentDescription = null,
                    modifier = Modifier.height(210.dp).width(210.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Elige tu Pokemon insignia!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ChoiseImage(statsViewModel)

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.width(200.dp).height(55.dp).fillMaxSize(),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                ) {
                    TextField(
                        value = userName,
                        textStyle =  MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                        onValueChange = {
                            userName = it
                            if (userName.length < 10) {
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
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,    // Fondo cuando está enfocado
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,  // Fondo cuando NO está enfocado
                            focusedTextColor = Color.Black,       // Texto ingresado
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.inverseSurface, // Placeholder enfocado
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.inverseSurface, // Placeholder desenfocado
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if(errorMessage != null){
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(64.dp))

                ConfirmButton(onConfirm = {
                    if (userName.isBlank()) {
                        errorMessage = "El nombre no puede estar vacío"
                    } else if (userName.length >= 10) {
                        errorMessage = "El nombre debe tener menos de 10 caracteres"
                    } else {
                        statsViewModel.registrarUserName(userName)
                        respondido = true
                    }
                }
                )
            }
        }
    }
    else{
        navController.navigate("home")
    }
}