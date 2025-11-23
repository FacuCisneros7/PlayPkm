package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.BannerAdd
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Ranking

@Composable
fun RankingScreen() {

    Box(Modifier.fillMaxSize()){
        GradientBackground(listOf(
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.onPrimary,
        ))

        Column(
            modifier = Modifier
            .padding(top = 16.dp, bottom = 60.dp)
            .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Box {
                // Contorno
                Text(
                    text = "RANKING",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 40.sp,
                        color = MaterialTheme.colorScheme.primary,
                        drawStyle = Stroke(width = 6f)
                    )
                )
                // Relleno
                Text(
                    text = "RANKING",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 40.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Ranking()

        }
        BannerAdd(Modifier.align(alignment = Alignment.BottomStart))
    }

}