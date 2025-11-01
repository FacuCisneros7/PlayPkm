package com.electrofire.playpkm.Data

data class ImpostorGameData(
    val abilityName: String? = null,
    val pokemons: List<PokemonApi> = emptyList(),
    val impostor: PokemonApi? = null
)