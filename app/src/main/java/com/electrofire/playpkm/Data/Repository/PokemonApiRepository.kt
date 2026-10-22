package com.electrofire.playpkm.Data.Repository

import android.util.Log
import com.electrofire.playpkm.Data.ImpostorGameData
import com.electrofire.playpkm.Data.ItemApi
import com.electrofire.playpkm.Data.LanguageGameData
import com.electrofire.playpkm.Data.LocalData.PokemonDao
import com.electrofire.playpkm.Data.LocalData.PokemonEntity
import com.electrofire.playpkm.Data.NetworkData.Ability
import com.electrofire.playpkm.Data.NetworkData.ApiPokemon
import com.electrofire.playpkm.Data.NetworkData.ItemResponse
import com.electrofire.playpkm.Data.NetworkData.ListPokemonAbilityResponse
import com.electrofire.playpkm.Data.NetworkData.PokemonResponse
import com.electrofire.playpkm.Data.PokemonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    // --- Funciones de Utilidad Interna ---

    private fun PokemonResponse.toPokemonApi(translatedAbilities: List<String>? = null): PokemonApi {
        return PokemonApi(
            name = name.replaceFirstChar { it.uppercase() },
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
            stats = stats.associate { it.stat.name to it.base_stat },
            abilities = translatedAbilities ?: abilities.map { it.ability.name },
            id = id,
            types = types.associate { it.type.name to it.slot },
            weight = weight
        )
    }

    private suspend fun getIdDelDia(seed: Int): Int {
        val horaServidor = timeRepository.obtenerHoraServidor() ?: return (1..1025).random()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        val allIds = (1..1025).toList().shuffled(Random(seed))
        return allIds[diaDelAnio % allIds.size]
    }

    private suspend fun translateAbilities(pokemon: PokemonResponse): List<String> {
        return pokemon.abilities.map { slot ->
            val abilityResponse = api.getAbilityByUrl(slot.ability.url)
            abilityResponse.names
                .firstOrNull { it.language.name == "es" }
                ?.name ?: slot.ability.name
        }
    }

    private fun ItemResponse.toItemApi(): ItemApi {
        val spanishName = names.firstOrNull { it.language.name == "es" }?.name ?: "Objeto Desconocido"
        val description = flavor_text_entries.find { it.language.name == "es" }?.text ?: ""
        
        // Traducimos categorías comunes a mano para mejor experiencia
        val translatedCategory = when(category.name) {
            "pokeballs" -> "Poké Ball"
            "medicine" -> "Medicina / Curación"
            "status-cures" -> "Cura de estado"
            "revival" -> "Revivir"
            "held-items" -> "Objeto para llevar"
            "evolution-stones" -> "Piedra evolutiva"
            "stat-boosters" -> "Mejora de estadísticas"
            else -> category.name.replace("-", " ").uppercase()
        }

        return ItemApi(
            name = spanishName,
            sprite = sprites.default,
            description = description,
            category = translatedCategory
        )
    }

    // --- Funciones Públicas ---

    suspend fun obtenerPokemonConMismaHabilidadDelDia(): ImpostorGameData {
        val idDelDia = getIdDelDia(1230)

        var pokemonList: ListPokemonAbilityResponse
        var ability: Ability

        // Selecciono una habilidad random con suficientes pokémon
        do {
            val listaAbilitys = api.getAllAbilities().results.orEmpty()
            ability = listaAbilitys.random()
            pokemonList = api.getPokemonByAbility(ability.name)
        } while (pokemonList.pokemon.size < 6)

        // Traer detalles de los Pokémon con esa habilidad en paralelo para mejorar performance
        val pokemonsConHabilidad = pokemonList.pokemon.orEmpty()
            .shuffled()
            .take(4)
            .map { it.pokemon }

        val pokemonConHabilidadCompletos = withContext(Dispatchers.IO) {
            pokemonsConHabilidad.map { p ->
                async { api.getPokemonByUrl(p.url) }
            }.awaitAll()
        }

        // Selecciono al pokemon SIN la habilidad (impostor)
        var pokemonSinHabilidad: PokemonResponse
        do {
            val p = api.getPokemon(idDelDia)
            pokemonSinHabilidad = p
        } while (p.abilities.any { it.ability.name == ability.name })

        // Traducir habilidad principal
        val translatedAbility = api.getAbilityByUrl(ability.url)
        val spanishName = translatedAbility.names?.firstOrNull { it.language.name == "es" }?.name ?: ability.name

        // Preparar Impostor y Lista
        val impostorAbilities = translateAbilities(pokemonSinHabilidad)
        val impostor = pokemonSinHabilidad.toPokemonApi(impostorAbilities)

        val shuffledResponses = (pokemonConHabilidadCompletos + pokemonSinHabilidad).shuffled()
        val listaPokemonCompleta = shuffledResponses.map { it.toPokemonApi() }

        return ImpostorGameData(
            abilityName = spanishName,
            pokemons = listaPokemonCompleta,
            impostor = impostor
        )
    }

    suspend fun obtenerPokemonRandom(): PokemonApi? {
        val randomId = (1..1025).random()
        return api.getPokemon(randomId).toPokemonApi()
    }

    suspend fun obtenerItemRandom(): ItemApi? {
        return try {
            var itemResponse: ItemResponse
            do {
                val randomId = (1..500).random() 
                itemResponse = api.getItem(randomId)
            } while (itemResponse.category.name == "all-machines") // Evitamos MTs y Máquinas

            itemResponse.toItemApi()
        } catch (e: Exception) {
            Log.e("ITEM_API", "Error al traer item: ${e.message}")
            null
        }
    }

    suspend fun obtenerPokemonDelDia(): PokemonApi? {
        val id = getIdDelDia(1234)
        return api.getPokemon(id).toPokemonApi()
    }

    suspend fun obtenerPokemonDelDiaConZoom(): PokemonApi? {
        val id = getIdDelDia(1534)
        return api.getPokemon(id).toPokemonApi()
    }

    suspend fun obtenerStatPokemonDelDia(): PokemonApi? {
        val id = getIdDelDia(1242)
        return api.getPokemon(id).toPokemonApi()
    }

    suspend fun obtenerHabilidadPokemonDelDia(): PokemonApi? {
        val id = getIdDelDia(1237)
        val response = api.getPokemon(id)
        val translated = translateAbilities(response)
        return response.toPokemonApi(translated)
    }

    suspend fun obtenerPokemonPorIdiomasDelDia(): LanguageGameData? {
        return try {
            val id = getIdDelDia(1625)
            val pokemonResponse = api.getPokemon(id)
            val speciesResponse = api.getPokemonSpecies(id)

            val japaneseName = speciesResponse.names.find { it.language.name == "ja" }?.name
                ?: speciesResponse.names.find { it.language.name == "ja-Hrkt" }?.name
                ?: "???"
            val germanName = speciesResponse.names.find { it.language.name == "de" }?.name ?: "???"
            val frenchName = speciesResponse.names.find { it.language.name == "fr" }?.name ?: "???"

            val pokemonApi = pokemonResponse.toPokemonApi()

            LanguageGameData(
                pokemon = pokemonApi,
                japaneseName = japaneseName,
                germanName = germanName,
                frenchName = frenchName
            )
        } catch (e: Exception) {
            Log.e("LANGUAGE_REPO", "Error al obtener pokemon por idiomas: ${e.message}")
            null
        }
    }

    suspend fun getRandomPokemons(count: Int = 3): List<PokemonApi> = withContext(Dispatchers.IO) {
        val randomIds = (1..1025).shuffled().take(count)
        randomIds.map { id ->
            async { api.getPokemon(id).toPokemonApi() }
        }.awaitAll()
    }

    suspend fun syncPokemon() {
        val response = api.getAllPokemon(limit = 1025, offset = 0)
        val allPokemonEntities = response.results.map { PokemonEntity(nombre = it.name) }
        dao.insertAll(allPokemonEntities)
    }

    suspend fun searchPokemon(query: String): List<PokemonEntity> {
        return dao.searchPokemon(query)
    }

    suspend fun isEmptyQuestion(): Boolean {
        return dao.count() == 0
    }
}
