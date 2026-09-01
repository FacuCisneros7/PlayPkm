package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.Data.UserData
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.common.RankingType
import com.google.firebase.auth.FirebaseAuth
import com.electrofire.playpkm.ui.Components.PokemonWithFlag

@Composable
fun RankingList(
    users: List<UserData>,
    type: RankingType,
    modifier: Modifier = Modifier
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    var selectedUser by remember { mutableStateOf<UserData?>(null) }

    val podiumUsers = users.take(3)
    val remainingUsers = users.drop(3)

    if (selectedUser != null) {
        UserDetailDialog(user = selectedUser!!, onDismiss = { selectedUser = null })
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            RankingPodium(users = podiumUsers, type = type, onUserClick = { selectedUser = it })
        }

        itemsIndexed(remainingUsers) { index, user ->
            val isCurrentUser = user.id == currentUserId
            RankingUserItem(
                user = user,
                position = index + 4,
                isCurrentUser = isCurrentUser,
                type = type,
                onClick = { selectedUser = user }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun RankingPodium(users: List<UserData>, type: RankingType, onUserClick: (UserData) -> Unit) {

    Surface(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
            ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            // 2do Puesto
            if (users.size >= 2) {
                PodiumItem(
                    user = users[1],
                    position = 2,
                    type = type,
                    modifier = Modifier.weight(1f).clickable { onUserClick(users[1]) }
                )
            }

            // 1er Puesto
            if (users.size >= 1) {
                PodiumItem(
                    user = users[0],
                    position = 1,
                    type = type,
                    modifier = Modifier.weight(1.3f).clickable { onUserClick(users[0]) }
                )
            }

            // 3er Puesto
            if (users.size >= 3) {
                PodiumItem(
                    user = users[2],
                    position = 3,
                    type = type,
                    modifier = Modifier.weight(1f).clickable { onUserClick(users[2]) }
                )
            }
        }
    }
}

@Composable
fun PodiumItem(user: UserData, position: Int, type: RankingType, modifier: Modifier = Modifier) {
    val (color, size) = when (position) {
        1 -> Color(0xFF8D3EB1) to 85.dp
        2 -> Color(0xFFF2C335) to 65.dp
        else -> Color(0xFF13B3F2) to 55.dp
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            // Avatar
            PokemonWithFlag(
                painter = user.imagen,
                nationality = user.nationality,
                imageSize = size,
                borderColor = color,
                borderWidth = 3.dp
            )
            
            // Medalla / Posición
            Card(
                modifier = Modifier.size(24.dp).padding(bottom = 2.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    when(position){
                        1 -> Image(
                            painter = painterResource(id = R.drawable.masterball),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(0.8f)
                        )
                        2 -> Image(
                            painter = painterResource(id = R.drawable.ultraball),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .alpha(0.8f)
                        )
                        3 -> Image(
                            painter = painterResource(id = R.drawable.superball),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .alpha(0.8f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = user.userName.orEmpty().uppercase(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        // Puntaje en podio
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (type == RankingType.WEEKLY) {
                // Victorias Semanales
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = "${user.weeklyWins}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(Modifier.width(8.dp))

                // Derrotas Semanales
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = "${user.weeklyLosses}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                // Otros Rankings (High Scores)
                val score = when (type) {
                    RankingType.GC -> user.maxPoints
                    RankingType.TS -> user.maxPointsDos
                    RankingType.BA -> user.maxPointsTres
                    else -> 0
                }
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "$score",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RankingUserItem(
    user: UserData,
    position: Int,
    type: RankingType,
    isCurrentUser: Boolean = false,
    onClick: () -> Unit = {}
) {
    val positionColor = when {
        position == 1 -> Color(0xFFFFC107)
        position <= 15 -> MaterialTheme.colorScheme.onPrimary
        else -> Color(0xFFBABECF)
    }

    val borderColor = if (isCurrentUser) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
    }

    Card(
        modifier = Modifier
            .width(320.dp)
            .height(60.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
        ),
        border = BorderStroke(if (isCurrentUser) 3.dp else 2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
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
                PokemonWithFlag(
                    painter = user.imagen,
                    nationality = user.nationality,
                    imageSize = 35.dp,
                    borderWidth = 0.dp
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
                    RankingType.WEEKLY -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${user.weeklyWins}",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${user.weeklyLosses}",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    RankingType.GC -> {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${user.maxPoints}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp))
                    }
                    RankingType.TS -> {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${user.maxPointsDos}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp))
                    }
                    RankingType.BA -> {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${user.maxPointsTres}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp))
                    }
                }
            }
        }
    }
}
