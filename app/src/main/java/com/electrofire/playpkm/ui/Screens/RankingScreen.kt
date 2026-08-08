package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.RankingList
import com.electrofire.playpkm.ui.ViewModels.RankingState
import com.electrofire.playpkm.ui.ViewModels.RankingType
import com.electrofire.playpkm.ui.ViewModels.RankingViewModel
import kotlinx.coroutines.launch

@Composable
fun RankingScreen(viewModel: RankingViewModel = hiltViewModel()) {
    val states by viewModel.states.collectAsState()
    val tabs = RankingType.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        GradientBackground(
            listOf(
                MaterialTheme.colorScheme.onPrimary,
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.onPrimary,
            )
        )

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
                        color = MaterialTheme.colorScheme.primary,
                        drawStyle = Stroke(width = 6f)
                    )
                )
                Text(
                    text = "RANKING",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 40.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

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
                    is RankingState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Loading()
                        }
                    }
                    is RankingState.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = state.message, color = Color.Red)
                        }
                    }
                    is RankingState.Success -> {
                        RankingList(users = state.users, type = type)
                    }
                    else -> {}
                }
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

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
                                    RankingType.GENERAL -> "GENERAL"
                                    RankingType.GC -> "GOOD CHOICE"
                                    RankingType.TS -> "THOUSAND SHADOWS"
                                    RankingType.BA -> "BEFORE AFTER"
                                },
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 12.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                )
                            )
                        }
                    )
                }
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

        }
    }
}