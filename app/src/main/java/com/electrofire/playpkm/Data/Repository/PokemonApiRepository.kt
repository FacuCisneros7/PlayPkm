package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.NetworkData.ApiPokemon
import com.electrofire.playpkm.Data.PokemonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PokemonApiRepository @Inject constructor(
    private val api: ApiPokemon
) {
    suspend fun getRandomPokemons(): List<PokemonApi> = withContext(Dispatchers.IO){
        val randomIds = (1..1025).shuffled().take(3)

        randomIds.map { id ->

            val response = api.getPokemon(id)

            val formattedName = response.name
                .lowercase()
                .replace(" ", "-")
                .replace(".", "")
                .replace("'", "")
                .replace("♀", "-f")
                .replace("♂", "-m")

            PokemonApi(
                name = response.name.replaceFirstChar{ it.uppercase() },
                //imageUrl = "https://img.pokemondb.net/artwork/large/${formattedName}.jpg",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                stats = response.stats.associate { it.stat.name to it.base_stat }
            )
        }
    }
}