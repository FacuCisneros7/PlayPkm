package com.electrofire.playpkm.ui.Scaffold

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar() {
    val context = LocalContext.current

    var showInfoCard by remember { mutableStateOf(false) }

    var isSelected by remember { mutableStateOf(false)}

        CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Playpkm",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    showInfoCard = true
                    isSelected = true
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.comentarioinfo),
                    contentDescription = "Info",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(if (isSelected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/pokememesespanol/")
                )
                context.startActivity(intent)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.instagram),
                    contentDescription = "Instagram",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )

    if (showInfoCard) {
        InfoDialog(onDismiss = {
            showInfoCard = false
            isSelected = false
        })
    }
}
