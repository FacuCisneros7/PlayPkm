package com.electrofire.playpkm.Domain

import android.content.Context

fun GetAppVersion(context: Context): String {
    return context.packageManager
        .getPackageInfo(context.packageName, 0)
        .versionName ?: "0.0.0"
}
