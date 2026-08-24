package com.electrofire.playpkm.ui.Components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@Composable
fun HomeStatsCard(modifier: Modifier = Modifier, statsViewModel: HomeStatsViewModel) {
    val stats = statsViewModel.userData

    Card(
        modifier = modifier.wrapContentSize(),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Victorias
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${stats.victorias}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val infiniteTransition = rememberInfiniteTransition(label = "streak")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = if (statsViewModel.rachaIncrementadaHoy) 1.2f else 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )

                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = if (stats.rachaActual > 0) MaterialTheme.colorScheme.surfaceVariant else Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .scale(scale)
                )
                Text(
                    text = if(stats.rachaActual == 1){
                        "${stats.rachaActual} DÍA"
                    }else{
                        "${stats.rachaActual} DÍAS"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (stats.rachaActual > 0) MaterialTheme.colorScheme.surfaceVariant else Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Derrotas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${stats.derrotas}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                )
            }
        }
    }
}