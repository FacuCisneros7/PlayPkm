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
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalConfiguration
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

    // Calculamos el ancho de la tarjeta basándonos en el ancho de la pantalla
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    // Queremos que entren 3 tarjetas, considerando el padding lateral (16*2) y el espacio entre tarjetas (16*2)
    val cardWidth = (screenWidth - 32.dp - 32.dp) / 2.8f // 2.8 para que se vea un poco de la 4ta o queden bien espaciadas

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
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
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

            Spacer(modifier = Modifier.height(24.dp))

            if (shopViewModel.isLoading) {
                Box(Modifier.height(400.dp), contentAlignment = Alignment.Center) {
                    Loading()
                }
            } else {
                // Sección Temporada Actual
                ShopSection(
                    title = "TEMPORADA ACTUAL",
                    items = shopViewModel.shopData.currentSeason,
                    userCoins = user.coins,
                    userProfileImages = user.profileImages,
                    cardWidth = cardWidth,
                    onBuyClick = { item ->
                        statsViewModel.registrarCompra(item.imageUrl, item.price)
                        showSuccessAnimation = true
                        Toast.makeText(context, "¡Pokémon adquirido!", Toast.LENGTH_SHORT).show()
                    },
                    context = context
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección Shinys
                ShopSection(
                    title = "SHINYS",
                    items = shopViewModel.shopData.shinys,
                    userCoins = user.coins,
                    userProfileImages = user.profileImages,
                    cardWidth = cardWidth,
                    onBuyClick = { item ->
                        statsViewModel.registrarCompra(item.imageUrl, item.price)
                        showSuccessAnimation = true
                        Toast.makeText(context, "¡Pokémon adquirido!", Toast.LENGTH_SHORT).show()
                    },
                    context = context
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección Legendarios
                ShopSection(
                    title = "LEGENDARIOS",
                    items = shopViewModel.shopData.legendaries,
                    userCoins = user.coins,
                    userProfileImages = user.profileImages,
                    cardWidth = cardWidth,
                    onBuyClick = { item ->
                        statsViewModel.registrarCompra(item.imageUrl, item.price)
                        showSuccessAnimation = true
                        Toast.makeText(context, "¡Pokémon adquirido!", Toast.LENGTH_SHORT).show()
                    },
                    context = context
                )
                
                Spacer(modifier = Modifier.height(32.dp))
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
fun ShopSection(
    title: String,
    items: List<ShopItem>,
    userCoins: Int,
    userProfileImages: List<String>,
    cardWidth: androidx.compose.ui.unit.Dp,
    onBuyClick: (ShopItem) -> Unit,
    context: android.content.Context
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Título estilizado como en Ranking
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Contenedor de la Fila (LazyRow) estilizado
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay items disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        ShopItemCard(
                            item = item,
                            isOwned = userProfileImages.contains(item.imageUrl),
                            canAfford = userCoins >= item.price,
                            cardWidth = cardWidth,
                            onBuyClick = {
                                if (userCoins >= item.price) {
                                    onBuyClick(item)
                                } else {
                                    Toast.makeText(context, "No tienes suficientes monedas", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShopItemCard(
    item: ShopItem,
    isOwned: Boolean,
    canAfford: Boolean,
    cardWidth: androidx.compose.ui.unit.Dp,
    onBuyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(150.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)),
        border = BorderStroke(2.dp, if (isOwned) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.tertiary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f))
                    .border(2.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                val painter = rememberAsyncImagePainter(
                    model = coil.request.ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                        .listener(
                            onError = { _, errorResult ->
                                android.util.Log.e("IMAGE_ERROR", "Error cargando item de tienda: ${item.name}")
                                android.util.Log.e("IMAGE_ERROR", "URL: ${item.imageUrl}")
                                android.util.Log.e("IMAGE_ERROR", "Motivo: ${errorResult.throwable.message}")
                            }
                        )
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentScale = ContentScale.Fit,
                )
            }

            Column(
                modifier = Modifier.height(70.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = item.name.uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    maxLines = 1
                )

                if (isOwned) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "ADQUIRIDO",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                } else {
                    Button(
                        onClick = onBuyClick,
                        enabled = canAfford,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                            .height(28.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        LottieAnimationView(
                            resId = R.raw.coin,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(1.dp))
                        Text(
                            text = item.price.toString(),
                            color = if (canAfford) MaterialTheme.colorScheme.primary else Color.Red,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("COMPRAR", fontSize = 10.sp, fontWeight = MaterialTheme.typography.titleLarge.fontWeight)
                    }
                }
            }

        }
    }
}
