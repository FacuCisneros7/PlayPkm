package com.electrofire.playpkm.ui.Scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar() {
    var showInfoCard by remember { mutableStateOf(false) }
    var showMissionsDialog by remember { mutableStateOf(false) }
    var isSelected by remember { mutableStateOf(false) }

    val statsViewModel: HomeStatsViewModel = hiltViewModel()
    val hasCompletedMissions =
        statsViewModel.userData.missions.any { !it.isClaimed && it.currentProgress >= it.targetProgress }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Playpkm",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    showInfoCard = true
                    isSelected = true
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.comentarioinfo),
                    contentDescription = "Info",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                showMissionsDialog = true
                isSelected = true
            }) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.objetivo),
                        contentDescription = "Misiones",
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary)
                    )
                    if (hasCompletedMissions) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color.Red, CircleShape)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )

    if (showInfoCard) {
        InfoDialog(onDismiss = {
            showInfoCard = false
            isSelected = false
        })
    }

    if (showMissionsDialog) {
        MissionsDialog(
            statsViewModel = statsViewModel,
            onDismiss = {
                showMissionsDialog = false
                isSelected = false
            }
        )
    }
}
