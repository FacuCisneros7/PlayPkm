package com.electrofire.playpkm.Data

import com.google.firebase.firestore.PropertyName

data class Mission(
    val id: String = "",
    val title: String = "",
    val currentProgress: Int = 0,
    val targetProgress: Int = 1,
    val rewardCoins: Int = 100,
    @get:PropertyName("claimed")
    var isClaimed: Boolean = false,
    val type: String = "DAILY" // "DAILY" o "WEEKLY"
)
