package com.electrofire.playpkm.Data

data class ShopData(
    val currentSeason: List<ShopItem> = emptyList(),
    val shinys: List<ShopItem> = emptyList(),
    val legendaries: List<ShopItem> = emptyList()
)
