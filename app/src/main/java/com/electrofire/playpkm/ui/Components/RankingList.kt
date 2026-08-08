package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Data.UserData
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.RankingType
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RankingList(
    users: List<UserData>,
    type: RankingType,
    modifier: Modifier = Modifier
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(users) { index, user ->
            val isCurrentUser = user.id == currentUserId
            RankingUserItem(
                user = user,
                position = index + 1,
                isCurrentUser = isCurrentUser,
                type = type
            )
        }
    }
}

@Composable
fun RankingUserItem(
    user: UserData,
    position: Int,
    type: RankingType,
    isCurrentUser: Boolean = false
) {
    val positionColor = when {
        position == 1 -> Color(0xFFFFC107)
        position <= 5 -> Color(0xFFF027FF)
        position <= 15 -> MaterialTheme.colorScheme.onPrimary
        else -> Color(0xFFBABECF)
    }

    val borderColor = if (isCurrentUser) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Card(
        modifier = Modifier
            .width(320.dp)
            .height(55.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        border = BorderStroke(3.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posición
            Text(
                text = "$position",
                color = positionColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.width(30.dp)
            )

            // Avatar
            Image(
                painter = rememberAsyncImagePainter(user.imagen),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Nombre
            Text(
                text = user.userName.orEmpty().uppercase(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )

            // Puntaje según el tipo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                when (type) {
                    RankingType.GENERAL -> {
                        Text(text = "W:", color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.titleMedium)
                        Text(text = "${user.victorias}", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 2.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "L:", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                        Text(text = "${user.derrotas}", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 2.dp))
                    }
                    RankingType.GC -> {
                        Text(text = "MAX: ", color = MaterialTheme.colorScheme.outline)
                        Text(text = "${user.maxPoints}", color = MaterialTheme.colorScheme.primary)
                    }
                    RankingType.TS -> {
                        Text(text = "MAX: ", color = MaterialTheme.colorScheme.outline)
                        Text(text = "${user.maxPointsDos}", color = MaterialTheme.colorScheme.primary)
                    }
                    RankingType.BA -> {
                        Text(text = "MAX: ", color = MaterialTheme.colorScheme.outline)
                        Text(text = "${user.maxPointsTres}", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun Spacer(modifier: Modifier) {
    androidx.compose.foundation.layout.Spacer(modifier)
}