package com.electrofire.playpkm.Domain

fun getAvatarAssetPath(avatarId: String): String {
    if (avatarId.startsWith("file://") || avatarId.startsWith("http://") || avatarId.startsWith("https://")) {
        return avatarId
    }
    return if (avatarId.startsWith("shiny_")) {
        val cleanId = avatarId.removePrefix("shiny_")
        "file:///android_asset/portraits_shiny/$cleanId.png"
    } else if (avatarId.endsWith("_shiny")) {
        val cleanId = avatarId.removeSuffix("_shiny")
        "file:///android_asset/portraits_shiny/$cleanId.png"
    } else {
        "file:///android_asset/portraits/$avatarId.png"
    }
}
