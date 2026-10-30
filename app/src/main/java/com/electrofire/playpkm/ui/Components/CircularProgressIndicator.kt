package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.R

@Composable
fun Loading() {
    LottieAnimationView(
        resId = R.raw.pokeballloading,
        modifier = Modifier.size(75.dp)
    )
}
