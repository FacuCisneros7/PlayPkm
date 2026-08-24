package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.electrofire.playpkm.ui.ViewModels.common.ContadorViewModel

@Composable
fun Contador(modifier: Modifier = Modifier, contadorViewModel: ContadorViewModel = viewModel()) {

    val contador = contadorViewModel.contador

    LaunchedEffect(Unit) {
        contadorViewModel.iniciarContador()
    }

    Card(
        modifier = modifier.wrapContentSize(),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = " $contador ",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}