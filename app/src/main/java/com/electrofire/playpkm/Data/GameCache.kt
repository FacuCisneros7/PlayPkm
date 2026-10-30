package com.electrofire.playpkm.Data

import com.google.firebase.Timestamp

data class GameCache(
    val carta: Carta? = null,
    val fusion: Fusion? = null,
    val lastUpdated: Timestamp? = null
)
