package com.electrofire.playpkm.Data

import com.google.firebase.Timestamp

data class RankingCache(
    val lastUpdated: Timestamp? = null,
    val general: List<UserData> = emptyList(),
    val goodChoice: List<UserData> = emptyList(),
    val thousandShadows: List<UserData> = emptyList(),
    val beforeAfter: List<UserData> = emptyList()
)
