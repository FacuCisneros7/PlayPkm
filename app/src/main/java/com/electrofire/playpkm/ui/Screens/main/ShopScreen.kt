package com.electrofire.playpkm.ui.Screens.main

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Data.ShopItem
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.LottieAnimationView
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.main.ShopViewModel
import kotlinx.coroutines.delay

@Composable
fun ShopScreen(
    statsViewModel: HomeStatsViewModel,
    shopViewModel: ShopViewModel = hiltViewModel()
) {
    val user = statsViewModel.userData
    val context = LocalContext.current
    var showSuccessAnimation by remember { mutableStateOf(false) }

    if (showSuccessAnimation) {
        LaunchedEffect(Unit) {
            delay(2000)
            showSuccessAnimation = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.tiendapokemon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = "TIENDA",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LottieAnimationView(
                        resId = R.raw.coin,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = user.coins.toString(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (shopViewModel.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(shopViewModel.shopItems) { item ->
                        ShopItemCard(
                            item = item,
                            isOwned = user.profileImages.contains(item.imageUrl),
                            canAfford = user.coins >= item.price,
                            onBuyClick = {
                                if (user.coins >= item.price) {
                                    statsViewModel.registrarCompra(item.imageUrl, item.price)
                                    showSuccessAnimation = true
                                    Toast.makeText(context, "¡Pokémon adquirido!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "No tienes suficientes monedas", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }

        if (showSuccessAnimation) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimationView(
                    resId = R.raw.successbuy,
                    modifier = Modifier.size(200.dp),
                    iterations = 1
                )
            }
        }
    }
}

@Composable
fun ShopItemCard(
    item: ShopItem,
    isOwned: Boolean,
    canAfford: Boolean,
    onBuyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)),
        border = BorderStroke(2.dp, if (isOwned) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.tertiary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen ocupando la parte superior con bordes redondeados arriba
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f))
                    .border(2.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                    Image(
                        painter = rememberAsyncImagePainter(item.imageUrl),
                        contentDescription = item.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentScale = ContentScale.Fit,
                    )
                }

            Spacer(Modifier.height(8.dp))
            
            Text(
                text = item.name.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                maxLines = 1
            )
            
            Spacer(Modifier.height(4.dp))
            
            if (isOwned) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = "ADQUIRIDO",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LottieAnimationView(
                        resId = R.raw.coin,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = item.price.toString(),
                        color = if (canAfford) MaterialTheme.colorScheme.primary else Color.Red,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onBuyClick,
                    enabled = canAfford,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                        .height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("COMPRAR", fontSize = 14.sp, fontWeight = MaterialTheme.typography.titleLarge.fontWeight)
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
