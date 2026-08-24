package com.electrofire.playpkm.ui.Components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Themes.PLAYPKMTheme


@Composable
fun MyCardButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    @DrawableRes imageRes: Int,
    showBadge: Boolean = false
) {
    BadgedBox(
        modifier = modifier,
        badge = {
            if (showBadge) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.outline,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CatchingPokemon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    ) {
        Card(
            modifier = Modifier
                .width(234.dp)
                .height(68.dp)
                .clickable { onClick() },
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = if (title == "THOUSAND SHADOWS") {
                        MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp)
                    } else {
                        MaterialTheme.typography.titleLarge
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun MyCardButtonPreview() {
    PLAYPKMTheme {
        MyCardButton(title = "¿Quién es este Pokemon?", imageRes = R.drawable.pokedex, onClick = {})
    }

}
