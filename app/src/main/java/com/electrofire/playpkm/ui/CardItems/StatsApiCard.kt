package com.electrofire.playpkm.ui.CardItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.ViewModels.StatsApiViewModel

@Composable
fun StatsApiCard(modifier: Modifier = Modifier, viewModel: StatsApiViewModel = hiltViewModel()) {
    val pokemon = viewModel.pokemon

    if (pokemon == null) {
        Card(
            modifier = modifier
                .width(290.dp)
                .height(154.dp)
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Loading()
            }
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.secondary.copy(0.8f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp)
            ) {

                pokemon.stats.forEach { (statName, statValue) ->
                    StatRow(statName = statName, statValue = statValue)
                }

            }

        }
    }
}

@Composable
fun StatRow(statName: String, statValue: Int, maxStat: Int = 200) {

    val progress = (statValue.toFloat() / maxStat).coerceIn(0f, 1f)
    var statNameRecorted = " "

    when (statName) {
        "special-attack" -> statNameRecorted = "SPA"
        "special-defense" -> statNameRecorted = "SPD"
        "attack" -> statNameRecorted = "ATK"
        "defense" -> statNameRecorted = "DEF"
        "speed" -> statNameRecorted = "SPE"
        "hp" -> statNameRecorted = "HP"
    }

    val statColor = when (statName.lowercase()) {
        "special-attack" -> Color(0xFF00C70A)
        "special-defense" -> Color(0xFFEF8D00)
        "attack" -> Color(0xFFC7221A)
        "defense" -> Color(0xFFE6D800)
        "speed" -> Color(0xFF0E3ADC)
        "hp" -> Color(0xFFDB63C9)
        else -> Color(0xFF0E3ADC)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = statNameRecorted,
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 15.sp)
        )

        Text(
            text = statValue.toString(),
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 15.sp)
        )

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = statColor,
            trackColor = MaterialTheme.colorScheme.inversePrimary
        )

    }

}