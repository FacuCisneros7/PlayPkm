package com.electrofire.playpkm.ui.Scaffold

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.MyCardButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar() {
    val context = LocalContext.current

    var showInfoCard by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Playpkm",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = { showInfoCard = true }) {
                Image(
                    painter = painterResource(id = R.drawable.comentarioinfo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/pokememesespanol/")
                )
                context.startActivity(intent)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.instagram),
                    contentDescription = "Logo",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )

    if (showInfoCard) {
        Dialog(onDismissRequest = { showInfoCard = false }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.7f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f)
                    )
                ) {

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {

                        item {
                            Box {
                                Text(
                                    text = stringResource(id = R.string.about_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = stringResource(id = R.string.about_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }

                        item {

                            Text(
                                text = stringResource(id = R.string.about_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            Box {
                                Text(
                                    text = stringResource(id = R.string.minigames_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = stringResource(id = R.string.minigames_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.minigames_general_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.before_or_after_title),
                                imageRes = R.drawable.beforeorafter,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.before_or_after_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.thousand_shadows_title),
                                imageRes = R.drawable.thousandshadows,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.thousand_shadows_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.good_choice_title),
                                imageRes = R.drawable.goodchoisenew,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.good_choice_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.zoom_game_title),
                                imageRes = R.drawable.adasdss,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.zoom_game_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.easy_game_title),
                                imageRes = R.drawable.asfasfasfa,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.easy_game_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.blurred_card_title),
                                imageRes = R.drawable.carta,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.blurred_card_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.one_ability_title),
                                imageRes = R.drawable.habilidad,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.one_ability_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.power_move_title),
                                imageRes = R.drawable.movimiento,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.power_move_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.mysterious_stats_title),
                                imageRes = R.drawable.movimientodos,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.mysterious_stats_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.fusion_title),
                                imageRes = R.drawable.fision,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.fusion_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.the_best_title),
                                imageRes = R.drawable.adasdad,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.the_best_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        item {
                            MyCardButton(
                                title = stringResource(id = R.string.impostor_title),
                                imageRes = R.drawable.dfsfsdf,
                                onClick = {},
                                modifier = Modifier
                                    .height(55.dp)
                                    .width(175.dp)
                            )
                            Spacer(Modifier.height(6.dp))

                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.impostor_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(24.dp))
                        }

                        item {
                            Box {
                                Text(
                                    text = stringResource(id = R.string.suggestion_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = stringResource(id = R.string.suggestion_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.suggestion_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        item {
                            Image(
                                painter = painterResource(id = R.drawable.sugerencia),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(175.dp)
                                    .wrapContentHeight()
                            )
                            Spacer(Modifier.height(32.dp))
                        }

                        item {
                            Box {
                                Text(
                                    text = stringResource(id = R.string.thanks_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = stringResource(id = R.string.thanks_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.thanks_description),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(32.dp))
                        }

                        item {
                            Box {
                                Text(
                                    text = stringResource(id = R.string.credits_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        drawStyle = Stroke(width = 4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = stringResource(id = R.string.credits_title),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.credits_description_1),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.credits_description_2),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.credits_description_3),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(32.dp))
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.credits_description_4),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(32.dp))
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.credits_author),
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                        }

                    }

                }


            }
        }
    }
}

