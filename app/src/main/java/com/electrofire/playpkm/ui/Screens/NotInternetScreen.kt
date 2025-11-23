package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.GradientBackground

@Composable
fun NotInternetScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        GradientBackground()

        Column(
            Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text(
                text = "ERROR DE CONEXION",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(48.dp))

            Image(
                painter = painterResource(id = R.drawable.rotom),
                contentDescription = null,
                modifier = Modifier.height(300.dp).wrapContentWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Pruebe la conexion y reinicie la aplicación",
                color = Color.Red,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )



        }

    }



}