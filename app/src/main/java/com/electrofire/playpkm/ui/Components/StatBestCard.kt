package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatBestCard(modifier: Modifier = Modifier, statNombre: String?, numeroStat: String?) {

    var statNombreCorto = statNombre

    when (statNombreCorto){
        "Ataque Especial" -> statNombreCorto = "ATQ.ESP"
        "Defensa Especial" -> statNombreCorto = "DEF.ESP"
        "Ataque Físico" -> statNombreCorto = "ATQ.FIS"
        "Defensa Física" -> statNombreCorto = "DEF.FIS"
        "Velocidad" -> statNombreCorto = "VEL"
        "PS" -> statNombreCorto = "PS"
    }

    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary.copy(0.3f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
        )
    ) {

        Row(
            modifier = Modifier
                .wrapContentWidth().fillMaxHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${statNombreCorto?.uppercase()}:",
                color = MaterialTheme.colorScheme.primary.copy(0.5f),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier.width(6.dp))
            Text(
                numeroStat?.uppercase() ?: "",
                color = MaterialTheme.colorScheme.primary.copy(0.5f),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                textAlign = TextAlign.Center
            )
        }

    }

}
