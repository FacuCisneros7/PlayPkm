package com.electrofire.playpkm.ui.Scaffold

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R

@Composable
fun GamesInfoSection() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            SectionTitle(stringResource(R.string.minigames_title))
            Text(
                text = stringResource(R.string.minigames_general_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(16.dp))
        }

        val games = listOf(
            GameInfo(R.string.zoom_game_title, R.string.zoom_game_description, R.drawable.zoomdos),
            GameInfo(R.string.easy_game_title, R.string.easy_game_description, R.drawable.easygamedos),
            GameInfo(R.string.blurred_card_title, R.string.blurred_card_description, R.drawable.carddos),
            GameInfo(R.string.type_game_title, R.string.type_game_description, R.drawable.typegamedos),
            GameInfo(R.string.item_game_title, R.string.item_game_description, R.drawable.itemgamedos),
            GameInfo(R.string.one_ability_title, R.string.one_ability_description, R.drawable.abilitydos),
            GameInfo(R.string.power_move_title, R.string.power_move_description, R.drawable.movepowerdos),
            GameInfo(R.string.mysterious_stats_title, R.string.mysterious_stats_description, R.drawable.statsdos),
            GameInfo(R.string.fusion_title, R.string.fusion_description, R.drawable.fusiondos),
            GameInfo(R.string.the_best_title, R.string.the_best_description, R.drawable.thebestdos),
            GameInfo(R.string.impostor_title, R.string.impostor_description, R.drawable.impostordos),
            GameInfo(R.string.good_choice_title, R.string.good_choice_description, R.drawable.goodchoisedos),
            GameInfo(R.string.thousand_shadows_title, R.string.thousand_shadows_description, R.drawable.thousandshadowsdos),
            GameInfo(R.string.before_or_after_title, R.string.before_or_after_description, R.drawable.beforeorafter)
        )

        items(games.size) { index ->
            GameInfoRow(games[index])
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun RankingInfoSection() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            SectionTitle(stringResource(R.string.ranking_info_title))
            Text(
                text = stringResource(R.string.ranking_info_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            RankingSubSection(
                title = stringResource(R.string.ranking_weekly_title),
                description = stringResource(R.string.ranking_weekly_desc)
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            RankingSubSection(
                title = "RANKING GOOD CHOISE",
                description = stringResource(R.string.ranking_weekly_desc)
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            RankingSubSection(
                title = "RANKING THOUSAND SHADOWS",
                description = stringResource(R.string.ranking_weekly_desc)
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            RankingSubSection(
                title = "RANKING BEFORE OR AFTER",
                description = stringResource(R.string.ranking_weekly_desc)
            )
        }
    }
}

@Composable
fun ShopInfoSection() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(stringResource(R.string.shop_info_title))
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.tiendapokemon),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.shop_info_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun UserInfoSection() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(stringResource(R.string.user_info_title))
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.garchomp),
            contentDescription = null,
            modifier = Modifier.size(80.dp).clip(CircleShape)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.user_info_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                drawStyle = Stroke(width = 4f)
            )
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.tertiary
            )
        )
    }
}

@Composable
fun RankingSubSection(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun GameInfoRow(game: GameInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(game.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = stringResource(game.titleRes),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(game.descRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 14.sp
            )
        }
    }
}

data class GameInfo(
    val titleRes: Int,
    val descRes: Int,
    @DrawableRes val imageRes: Int
)