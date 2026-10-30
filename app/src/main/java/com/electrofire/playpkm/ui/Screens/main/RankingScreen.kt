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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.RankingList
import com.electrofire.playpkm.ui.ViewModels.common.RankingType
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import com.electrofire.playpkm.ui.ViewModels.main.ShopContadorViewModel
import com.electrofire.playpkm.ui.ViewModels.main.RankingViewModel
import kotlinx.coroutines.launch

@Composable
fun RankingScreen(
    viewModel: RankingViewModel = hiltViewModel(),
    shopContadorViewModel: ShopContadorViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsState()
    val tabs = RankingType.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val timeLeft by shopContadorViewModel.timeLeft.collectAsState()

    LaunchedEffect(Unit) {
        shopContadorViewModel.startCountdown()
    }

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

            // Temporizador de Semana estético (igual al de shop y home)
            val currentType = tabs[pagerState.currentPage]
            if (currentType == RankingType.WEEKLY) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Renovación: ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                    Text(
                        text = timeLeft,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ScrollableTabRow con estilo de pastillas (pills) arriba
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
                        }
                    ) {
                        Surface(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.5f
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
                                    alpha = 0.5f
                                )
                            ),
                            shadowElevation = if (isSelected) 4.dp else 0.dp
                        ) {
                            Text(
                                text = when (type) {
                                    RankingType.WEEKLY -> "SEMANAL"
                                    RankingType.GC -> "GOOD CHOICE"
                                    RankingType.TS -> "T. SHADOWS"
                                    RankingType.BA -> "BEFORE AFTER"
                                },
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.5f
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
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

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
