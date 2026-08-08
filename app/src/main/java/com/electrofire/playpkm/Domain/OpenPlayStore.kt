package com.electrofire.playpkm.Domain

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun OpenPlayStore(activity: Activity) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=${activity.packageName}")
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    activity.startActivity(intent)
}
