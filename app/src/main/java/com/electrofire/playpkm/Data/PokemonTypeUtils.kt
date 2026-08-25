package com.electrofire.playpkm.Data

import com.electrofire.playpkm.R

data class PokemonType(
    val englishName: String,
    val spanishName: String,
    val typeRes: Int
)

object PokemonTypeUtils {
    val types = listOf(
        PokemonType("normal", "Normal", R.drawable.tiponormal),
        PokemonType("fire", "Fuego", R.drawable.tipofuego),
        PokemonType("water", "Agua",R.drawable.tipoagua),
        PokemonType("grass", "Planta", R.drawable.tipoplanta),
        PokemonType("electric", "Eléctrico", R.drawable.tipoelectrico),
        PokemonType("ice", "Hielo", R.drawable.tipohielo),
        PokemonType("fighting", "Lucha", R.drawable.tipolucha),
        PokemonType("poison", "Veneno", R.drawable.tipoveneno),
        PokemonType("ground", "Tierra", R.drawable.tipotierra),
        PokemonType("flying", "Volador", R.drawable.tipovolador),
        PokemonType("psychic", "Psíquico", R.drawable.tipopsiquico),
        PokemonType("bug", "Bicho",R.drawable.tipobicho),
        PokemonType("rock", "Roca", R.drawable.tiporoca),
        PokemonType("ghost", "Fantasma", R.drawable.tipofantasma),
        PokemonType("dragon", "Dragón", R.drawable.tipodragon),
        PokemonType("dark", "Siniestro", R.drawable.tiposiniestro),
        PokemonType("steel", "Acero", R.drawable.tipoacero),
        PokemonType("fairy", "Hada", R.drawable.tipohada)
    )

    val typesWithEmpty = types + PokemonType("none", "No tiene", R.drawable.tipoastral)

    fun getTypeRes(englishName: String?): Int{
        return types.find { it.englishName.lowercase() == englishName?.lowercase() }?.typeRes ?: R.drawable.tipoastral
    }
}
