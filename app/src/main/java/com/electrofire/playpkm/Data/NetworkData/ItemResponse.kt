package com.electrofire.playpkm.Data.NetworkData

data class ItemResponse(
    val sprites: ItemSprite,
    val names: List<ItemName>,
    val category: ItemCategory
)

data class ItemCategory(
    val name: String,
    val url: String
)

data class ItemSprite(
    val default: String
)

data class ItemName(
    val language: ItemLanguage,
    val name: String
)

data class ItemLanguage(
    val name: String
)