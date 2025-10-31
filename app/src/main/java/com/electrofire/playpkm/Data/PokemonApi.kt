package com.electrofire.playpkm.Data

data class PokemonApi(
    val name: String,
    val imageUrl: String?,
    val stats: Map<String, Int>
)