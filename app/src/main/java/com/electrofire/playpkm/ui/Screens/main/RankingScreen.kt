package com.electrofire.playpkm.ui.Screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.RankingList
import com.electrofire.playpkm.ui.ViewModels.common.RankingType
import com.electrofire.playpkm.ui.ViewModels.main.RankingViewModel
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

@Composable
fun RankingScreen(viewModel: RankingViewModel = hiltViewModel()) {
    val states by viewModel.states.collectAsState()
    val tabs = RankingType.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    text = "RANKING",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 40.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // Temporizador de Semana más estético
            val currentType = tabs[pagerState.currentPage]
            
            if (currentType == RankingType.WEEKLY) {
                val daysUntilMonday = remember {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    val days = (9 - dayOfWeek) % 7
                    if (days == 0) 7 else days
                }

                Surface(
                    modifier = Modifier.padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RECOMPENSAS EN: $daysUntilMonday ${if (daysUntilMonday == 1) "DÍA" else "DÍAS"}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contenido Deslizable (Pager)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) { page ->
                val type = tabs[page]
                when (val state = states[type]) {
                    is UIState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Loading()
                        }
                    }
                    is UIState.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = state.message, color = Color.Red)
                        }
                    }
                    is UIState.Success -> {
                        RankingList(users = state.data, type = type)
                    }
                    else -> {}
                }
            }

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 16.dp,
                divider = {},
                indicator = {}
            ) {
                tabs.forEachIndexed { index, type ->
                    val isSelected = pagerState.currentPage == index
                    Tab(
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = when(type) {
                                    RankingType.WEEKLY -> "SEMANAL"
                                    RankingType.GC -> "GOOD CHOICE"
                                    RankingType.TS -> "THOUSAND SHADOWS"
                                    RankingType.BA -> "BEFORE AFTER"
                                },
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 12.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                )
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
