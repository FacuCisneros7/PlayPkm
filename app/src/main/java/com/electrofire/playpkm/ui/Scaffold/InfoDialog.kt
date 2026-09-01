package com.electrofire.playpkm.ui.Scaffold

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.electrofire.playpkm.R

enum class InfoCategory {
    GAMES, RANKING, SHOP, USER
}

@Composable
fun InfoDialog(onDismiss: () -> Unit) {
    var selectedCategory by remember { mutableStateOf(InfoCategory.GAMES) }

    Dialog(onDismissRequest = onDismiss) {
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
            Column(modifier = Modifier.fillMaxSize()) {
                // Header con Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CategoryIcon(
                        iconRes = R.drawable.palancademando,
                        title = stringResource(R.string.category_games),
                        isSelected = selectedCategory == InfoCategory.GAMES,
                        onClick = { selectedCategory = InfoCategory.GAMES }
                    )
                    CategoryIcon(
                        iconRes = R.drawable.podiodos,
                        title = stringResource(R.string.category_ranking),
                        isSelected = selectedCategory == InfoCategory.RANKING,
                        onClick = { selectedCategory = InfoCategory.RANKING }
                    )
                    CategoryIcon(
                        iconRes = R.drawable.tienda,
                        title = stringResource(R.string.category_shop),
                        isSelected = selectedCategory == InfoCategory.SHOP,
                        onClick = { selectedCategory = InfoCategory.SHOP }
                    )
                    CategoryIcon(
                        iconRes = R.drawable.perfil,
                        title = stringResource(R.string.category_user),
                        isSelected = selectedCategory == InfoCategory.USER,
                        onClick = { selectedCategory = InfoCategory.USER }
                    )
                }

                // Contenido dinámico
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    when (selectedCategory) {
                        InfoCategory.GAMES -> GamesInfoSection()
                        InfoCategory.RANKING -> RankingInfoSection()
                        InfoCategory.SHOP -> ShopInfoSection()
                        InfoCategory.USER -> UserInfoSection()
                    }
                }

                // Botón Cerrar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CERRAR",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(top = 8.dp, bottom =16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryIcon(
    @DrawableRes iconRes: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(if(isSelected)MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 9.sp
        )
    }
}
