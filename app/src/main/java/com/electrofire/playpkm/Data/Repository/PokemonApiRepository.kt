package com.electrofire.playpkm.Data.Repository

import android.util.Log
import com.electrofire.playpkm.Data.ImpostorGameData
import com.electrofire.playpkm.Data.LocalData.PokemonDao
import com.electrofire.playpkm.Data.LocalData.PokemonEntity
import com.electrofire.playpkm.Data.NetworkData.Ability
import com.electrofire.playpkm.Data.NetworkData.AbilityListResponse
import com.electrofire.playpkm.Data.NetworkData.ApiPokemon
import com.electrofire.playpkm.Data.NetworkData.ListPokemonAbilityResponse
import com.electrofire.playpkm.Data.NetworkData.PokemonResponse
import com.electrofire.playpkm.Data.PokemonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import kotlin.random.Random


class PokemonApiRepository @Inject constructor(
    private val api: ApiPokemon,
    private val dao: PokemonDao
) {

    private val timeRepository = TimeRepository()

    suspend fun obtenerPokemonConMismaHabilidadDelDia(): ImpostorGameData {

        val horaServidor = timeRepository.obtenerHoraServidor()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor!!
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        // la semilla asegura que todos vean lo mismo
        val allIds = (1..1025).shuffled(Random(1230))

        // Selecciono el ID según el día del año
        val idDelDia = allIds[diaDelAnio % allIds.size]

        //Variable que tendra pokemon con dicha habilidad.
        var pokemonList: ListPokemonAbilityResponse
        var abilityName: String
        var ability: Ability

        //Selecciono una habilidad random
        do {
            // Elegir una habilidad al azar de la lista
            val listaAbilitys = api.getAllAbilities().results.orEmpty()
            ability = listaAbilitys.random()
            abilityName = ability.name
            // Traer los detalles de esa habilidad (incluye lista de Pokémon)
            pokemonList = api.getPokemonByAbility(ability.name)

        } while (pokemonList.pokemon.size < 4)

        //Selecciono los 4 pokemon
        val pokemonsConHabilidad = pokemonList.pokemon.orEmpty()
            .shuffled()
            .take(4)
            .map { it.pokemon }

        val pokemonConHabilidadCompletos = pokemonsConHabilidad.map { p ->
            api.getPokemonByUrl(p.url)
        }

        //Selecciono al pokemon SIN la habilidad
        var pokemonSinHabilidad: PokemonResponse?
        do {
            val p = api.getPokemon(idDelDia)
            // verificamos que NO tenga la habilidad
            pokemonSinHabilidad = if (p.abilities.none { it.ability.name == abilityName }) p else null
        } while (pokemonSinHabilidad == null)

        val listPokemonResponse = (pokemonConHabilidadCompletos + pokemonSinHabilidad).shuffled()

        //Traducir habilidad
        val translatedAbility =  api.getAbilityByUrl(ability.url)
        val spanishName = translatedAbility.names?.firstOrNull { it.language.name == "es" } ?.name ?: ability.name

        //Traducir habilidades del pokemon impostor
        val translatedAbilities = pokemonSinHabilidad.abilities.map { slot ->
            val abilityResponse = api.getAbilityByUrl(slot.ability.url)
            val spanishNames = abilityResponse.names
                .firstOrNull { it.language.name == "es" }
                ?.name ?: slot.ability.name  // si no hay traducción, usa el nombre original
            spanishNames
        }

        val impostor = PokemonApi(
            name = pokemonSinHabilidad.name,
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonSinHabilidad.id}.png",
            stats = pokemonSinHabilidad.stats.associate { it.stat.name to it.base_stat },
            abilities = translatedAbilities
        )

        val listaPokemonCompleta: List<PokemonApi> = listPokemonResponse.map { pokemon ->
            PokemonApi(
                name = pokemon.name,
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
                stats = pokemon.stats.associate { it.stat.name to it.base_stat },
                abilities = pokemon.abilities.map { it.ability.name }
            )
        }

        return ImpostorGameData(
            abilityName = spanishName,
            pokemons = listaPokemonCompleta,
            impostor = impostor
        )

    }

    suspend fun obtenerPokemonDelDia(): PokemonApi? {

        val horaServidor = timeRepository.obtenerHoraServidor()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor!!  // ✅ horaServidor es no-null aquí
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        val allIds = (1..1025).shuffled(Random(1234)) // la semilla asegura que todos vean lo mismo

        // Selecciono el ID según el día del año
        val idDelDia = allIds[diaDelAnio % allIds.size]

        val response = api.getPokemon(idDelDia)

        return PokemonApi(
            name = response.name.replaceFirstChar { it.uppercase() },
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$idDelDia.png",
            stats = response.stats.associate { it.stat.name to it.base_stat },
            abilities = response.abilities.map { it.ability.name }
        )


    }

    suspend fun obtenerHabilidadPokemonDelDia(): PokemonApi ?{

        val horaServidor = timeRepository.obtenerHoraServidor()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor!!  // ✅ horaServidor es no-null aquí
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        val allIds = (1..1025).shuffled(Random(1237)) // la semilla asegura que todos vean lo mismo

        // Selecciono el ID según el día del año
        val idDelDia = allIds[diaDelAnio % allIds.size]

        val response = api.getPokemon(idDelDia)

        val translatedAbilities = response.abilities.map { slot ->
            val abilityResponse = api.getAbilityByUrl(slot.ability.url)
            val spanishName = abilityResponse.names
                .firstOrNull { it.language.name == "es" }
                ?.name ?: slot.ability.name  // si no hay traducción, usa el nombre original
            spanishName
        }

        return PokemonApi(
            name = response.name.replaceFirstChar { it.uppercase() },
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$idDelDia.png",
            stats = response.stats.associate { it.stat.name to it.base_stat },
            abilities = translatedAbilities
        )
    }

    suspend fun getRandomPokemons(): List<PokemonApi> = withContext(Dispatchers.IO) {
        val randomIds = (1..1025).shuffled().take(3)

        randomIds.map { id ->

            val response = api.getPokemon(id)

            PokemonApi(
                name = response.name.replaceFirstChar { it.uppercase() },
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                stats = response.stats.associate { it.stat.name to it.base_stat },
                abilities = response.abilities.map { it.ability.name }            )
        }
    }

    suspend fun syncPokemon(){
        val response = api.getAllPokemon(limit = 1025, offset = 0)
        val allPokemonEntities = response.results.map { PokemonEntity(nombre = it.name) }

        dao.insertAll(allPokemonEntities)
    }

    suspend fun searchPokemon(query: String): List<PokemonEntity> {
        return dao.searchPokemon(query)
    }

    //Verificar si la base local está vacía
    suspend fun isEmpty(): Boolean {
        return dao.count() == 0
    }

}