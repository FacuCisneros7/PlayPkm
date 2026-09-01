package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.electrofire.playpkm.Data.WeeklyRewardData
import com.electrofire.playpkm.R

@Composable
fun WeeklyRewardsDialog(
    rewardData: WeeklyRewardData,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título con Stroke
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "RECOMPENSAS",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.primary,
                            drawStyle = Stroke(width = 6f)
                        )
                    )
                    Text(
                        text = "RECOMPENSAS",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "RESUMEN SEMANAL",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats de la semana pasada
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatDisplay(
                            label = "WINS",
                            value = rewardData.wins,
                            icon = Icons.Default.CheckCircle,
                            color = MaterialTheme.colorScheme.outline
                        )
                        
                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )

                        StatDisplay(
                            label = "LOSS",
                            value = rewardData.losses,
                            icon = Icons.Default.Close,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Recompensa en Monedas
                Text(
                    text = "MONEDAS OBTENIDAS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(8.dp),
                        )
                    {
                        Text(
                            text = "${rewardData.coinsEarned}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 32.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        LottieAnimationView(
                            resId = R.raw.coin,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón reclamar
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "¡GENIAL!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun StatDisplay(label: String, value: Int, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
        }
        Text(text = value.toString(), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    }
}
