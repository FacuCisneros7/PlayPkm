package com.electrofire.playpkm.ui.Components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.Themes.PLAYPKMTheme


@Composable
fun MyCardButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    @DrawableRes imageRes: Int
) {
    Card(
        modifier = modifier
            .width(234.dp)
            .height(68.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
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
                style = MaterialTheme.typography.titleLarge
            )

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
