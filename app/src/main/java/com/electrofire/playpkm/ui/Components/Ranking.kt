package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.electrofire.playpkm.Data.UserData
import com.electrofire.playpkm.ui.ViewModels.RankingViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun Ranking(viewModel: RankingViewModel = viewModel()) {

    val users by viewModel.users.collectAsState()

    val currentUserId = Firebase.auth.currentUser?.uid

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(users) { index, user ->
            val isCurrentUser = user.id == currentUserId
            UserItem(user = user, position = index + 1, isCurrentUser = isCurrentUser)
        }
    }

}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: UserData,
    position: Int,
    isCurrentUser: Boolean = false
) {

    val color = when {
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
        modifier = modifier
            .width(320.dp)
            .height(50.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        border = BorderStroke(3.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Column(modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$position",
                    color = color,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Column(modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(user.imagen),
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier.width(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = user.userName.toString().uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp)
                )
            }

            Row(
                modifier = Modifier.width(130.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier.width(25.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "W:",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${user.victorias}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }


                Column(modifier.width(25.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "L:",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )

                }
                Column(modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${user.derrotas} ",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

    }
}