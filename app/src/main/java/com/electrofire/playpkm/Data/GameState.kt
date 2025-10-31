package com.electrofire.playpkm.Data

data class GameState(
    val pokemons: List<PokemonApi> = emptyList(),
    val selectedStat: String? = null,
    val selectedStatEnglish: String? = null,
    val correctPokemon: PokemonApi? = null
)