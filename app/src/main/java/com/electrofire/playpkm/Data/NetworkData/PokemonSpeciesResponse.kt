package com.electrofire.playpkm.Data.NetworkData

data class PokemonSpeciesResponse(
    val id: Int,
    val name: String,
    val names: List<LocalizedName>
)

data class LocalizedName(
    val name: String,
    val language: LanguageName
)

data class LanguageName(
    val name: String
)