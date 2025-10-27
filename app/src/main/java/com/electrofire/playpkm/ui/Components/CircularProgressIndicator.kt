package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Loading() {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.secondary,
        strokeWidth = 8.dp,
        modifier = Modifier.size(50.dp)// Grosor del círculo
    )
}
