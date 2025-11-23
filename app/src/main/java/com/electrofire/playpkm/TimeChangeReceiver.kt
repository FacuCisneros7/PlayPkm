package com.electrofire.playpkm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class TimeChangeReceiver(
    private val onTimeChanged: () -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {
                onTimeChanged()
                Toast.makeText(context, "Se detectó cambio de fecha/hora", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
