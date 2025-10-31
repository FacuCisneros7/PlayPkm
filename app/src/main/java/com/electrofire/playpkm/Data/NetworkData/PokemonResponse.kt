package com.electrofire.playpkm.Data.NetworkData

data class PokemonResponse(
    val name: String,
    val stats: List<Stat>
)

data class Stat(
    val base_stat: Int,
    val stat: StatName
)

data class StatName(
    val name: String
)