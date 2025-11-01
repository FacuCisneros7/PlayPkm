package com.electrofire.playpkm.Data.NetworkData

data class PokemonResponse(
    val name: String,
    val stats: List<Stat>,
    val abilities: List<AbilitySlot>,
    val id: Int,
    val url: String
)

data class Stat(
    val base_stat: Int,
    val stat: StatName
)

data class StatName(
    val name: String
)

data class AbilitySlot(
    val ability: Ability
)



data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
)
