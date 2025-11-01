package com.electrofire.playpkm.Data.NetworkData

data class AbilityResponse(
    val names: List<AbilityName>
)

data class AbilityName(
    val name: String,
    val language: Language
)

data class Language(
    val name: String
)


data class AbilityListResponse(
    val results: List<Ability>
)

data class ListPokemonAbilityResponse(
    val name: String,
    val pokemon: List<PokemonAbilityEntry>
)

data class PokemonAbilityEntry(
    val is_hidden: Boolean,
    val pokemon: PokemonResponse
)

data class Ability(
    val name: String,
    val url: String
)