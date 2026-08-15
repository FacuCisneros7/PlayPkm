package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel

@Composable
fun HomeStatsCard(modifier: Modifier = Modifier, statsViewModel: HomeStatsViewModel) {
    val stats = statsViewModel.userData

    Card(
        modifier = modifier.wrapContentSize(),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(8.dp),
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

            // Separador visual o simplemente espacio
            Text(text = "|", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))

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