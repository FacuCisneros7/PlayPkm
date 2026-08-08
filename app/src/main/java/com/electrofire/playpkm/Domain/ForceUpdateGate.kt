package com.electrofire.playpkm.Domain

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.electrofire.playpkm.ui.Components.Loading
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings


@Composable
fun ForceUpdateGate(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity

    var mustUpdate by remember { mutableStateOf(false) }
    var updateMessage by remember {
        mutableStateOf("Es necesario actualizar la app para continuar")
    }
    var isChecked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 🔧 debug
            }
        )

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            val minVersion = remoteConfig.getString("min_app_version")
            val forceUpdate = remoteConfig.getBoolean("force_update")
            val message = remoteConfig.getString("update_message")

            val currentVersion = GetAppVersion(context)

            mustUpdate =
                forceUpdate && isVersionLower(currentVersion, minVersion)

            if (message.isNotEmpty()) {
                updateMessage = message
            }

            isChecked = true
        }
    }

    if (!isChecked) {
        // Loading inicial
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Loading()
        }
    } else {
        if (mustUpdate) {
            ForceUpdateDialog(
                message = updateMessage,
                onUpdate = {
                    OpenPlayStore(activity)
                }
            )
        } else {
            content()
        }
    }
}
