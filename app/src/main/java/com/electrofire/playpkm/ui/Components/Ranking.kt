package com.electrofire.playpkm.ui.Components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.core.net.toUri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.electrofire.playpkm.Data.UserData
import com.electrofire.playpkm.ui.ViewModels.RankingViewModel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter


@Composable
fun Ranking(viewModel : RankingViewModel = viewModel()){

    val users = viewModel.users

    LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        itemsIndexed(users) {index, user ->
            UserItem(user = user, position = index + 1)
        }
    }

}

@Composable
fun UserItem(modifier: Modifier = Modifier, user: UserData, position : Int){

    Card(
        modifier = modifier.width(320.dp).height(50.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){

            Column(modifier.width(32.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$position", color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.titleLarge )
            }

            Column(modifier.width(65.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(user.imagen),
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier.width(110.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = user.userName.toString().uppercase(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp) )
            }

            Column(modifier.width(50.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "W: ${user.victorias} ", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge )
            }

            Column(modifier.width(50.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "L: ${user.derrotas} ", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge )

            }
        }

    }
}