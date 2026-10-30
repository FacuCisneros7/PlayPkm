package com.electrofire.playpkm.ui.Scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.LottieAnimationView
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MissionsDialog(
    statsViewModel: HomeStatsViewModel,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var showCoinBurst by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val missions = statsViewModel.userData.missions
    val dailyMissions = missions.filter { it.type == "DAILY" }
    val weeklyMissions = missions.filter { it.type == "WEEKLY" }

    LaunchedEffect(Unit) {
        visible = true
    }

    val dismissWithAnimation: () -> Unit = {
        coroutineScope.launch {
            visible = false
            delay(200)
            onDismiss()
        }
    }

    Dialog(onDismissRequest = dismissWithAnimation) {
        Box(contentAlignment = Alignment.Center) {
            AnimatedVisibility(
                visible = visible,
                enter = scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(durationMillis = 200)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 200)
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.85f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.tertiary),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.95f)
                    )
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {
                        // Título del Dialog
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "MISIONES",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (missions.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Por favor, reinicia la aplicación para cargar tus misiones",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            } else {
                                // Sección Diarias
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Diarias",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                        Text(
                                            text = "Se renuevan en 10 h 43 min",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        )
                                    }
                                    Spacer(Modifier.height(8.dp))
                                }

                                items(dailyMissions) { mission ->
                                    MissionCard(
                                        mission = mission,
                                        onCanjear = {
                                            statsViewModel.canjearMision(mission.id)
                                            showCoinBurst = true
                                            coroutineScope.launch {
                                                delay(1500)
                                                showCoinBurst = false
                                            }
                                        }
                                    )
                                }

                                // Sección Semanales
                                item {
                                    Spacer(Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Semanales",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                        Text(
                                            text = "Se renuevan en 2 d 10 h",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        )
                                    }
                                    Spacer(Modifier.height(8.dp))
                                }

                                items(weeklyMissions) { mission ->
                                    MissionCard(
                                        mission = mission,
                                        onCanjear = {
                                            statsViewModel.canjearMision(mission.id)
                                            showCoinBurst = true
                                            coroutineScope.launch {
                                                delay(1500)
                                                showCoinBurst = false
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Botón Cerrar
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "CERRAR",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable { dismissWithAnimation() }
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            if (showCoinBurst) {
                LottieAnimationView(
                    resId = R.raw.coinbust,
                    modifier = Modifier.size(280.dp)
                )
            }
        }
    }
}

@Composable
fun MissionCard(
    mission: com.electrofire.playpkm.Data.Mission,
    onCanjear: () -> Unit
) {
    val progress = if (mission.targetProgress > 0) {
        (mission.currentProgress.toFloat() / mission.targetProgress.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val canClaim = !mission.isClaimed && mission.currentProgress >= mission.targetProgress

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = mission.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${mission.currentProgress}/${mission.targetProgress}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (!mission.isClaimed) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "${mission.rewardCoins}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            LottieAnimationView(
                                resId = R.raw.coin,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                    }

                    Button(
                        onClick = onCanjear,
                        enabled = canClaim,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mission.isClaimed) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.2f
                            )
                            else if (canClaim) MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.tertiary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = if (mission.isClaimed) "Canjeado" else "Canjear",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (mission.isClaimed) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.4f
                            ) else MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}
