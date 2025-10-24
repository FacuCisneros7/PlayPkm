package com.electrofire.playpkm.Data.Repository

import androidx.room.Dao
import androidx.room.Room
import com.electrofire.playpkm.Data.LocalData.PokemonDao
import com.electrofire.playpkm.Data.LocalData.PokemonDatabase
import com.electrofire.playpkm.Data.LocalData.PokemonEntity
import com.electrofire.playpkm.Data.Pokemon
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import kotlin.random.Random

class PokemonRepository @Inject constructor(
    private val dao: PokemonDao
){

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Pokemon")

    suspend fun obtenerTodosLosPokemon(): List<Pokemon>{
        val snapshot = collection.get().await()  //Snapshot contiene_todo lo devuelto en la coleccion
        return snapshot.toObjects(Pokemon::class.java)
    }

    suspend fun obtenerPokemonDelDia(): Pokemon ?{
        val lista = obtenerTodosLosPokemon()
        val diaDelAnio = LocalDate.now().dayOfYear
        val random = Random(diaDelAnio.toLong())
        val indice = random.nextInt(lista.size)
        return lista[indice]
    }

    suspend fun obtenerHabilidadPokemonDelDia(): Pokemon ?{
        val pokemonDia = obtenerPokemonDelDia()
        val lista = obtenerTodosLosPokemon()
        val listaFiltrada = lista.filter { it != pokemonDia }
        val diaDelAnio = LocalDate.now().dayOfYear
        val random = Random(diaDelAnio.toLong() + 1000)
        val indice = random.nextInt(listaFiltrada.size)
        return listaFiltrada[indice]
    }

    suspend fun obtenerStatsPokemonDelDia(): Pokemon ?{
        val pokemonDia = obtenerPokemonDelDia()
        val habilidadDia = obtenerHabilidadPokemonDelDia()
        val lista = obtenerTodosLosPokemon()
        val listaFiltrada = lista.filter { it != pokemonDia && it != habilidadDia}
        val diaDelAnio = LocalDate.now().dayOfYear
        val random = Random(diaDelAnio.toLong() + 2000)
        val indice = random.nextInt(listaFiltrada.size)
        return listaFiltrada[indice]
    }

    suspend fun obtenerPokemonSiluetaUnoDia(): Pokemon ?{
        val pokemonDia = obtenerPokemonDelDia()
        val habilidadDia = obtenerHabilidadPokemonDelDia()
        val statsDia = obtenerStatsPokemonDelDia()
        val lista = obtenerTodosLosPokemon()
        val listaFiltrada = lista.filter { it != pokemonDia && it != habilidadDia && it != statsDia}
        val diaDelAnio = LocalDate.now().dayOfYear
        val random = Random(diaDelAnio.toLong() + 3000)
        val indice = random.nextInt(listaFiltrada.size)
        return listaFiltrada[indice]
    }

    suspend fun obtenerPokemonSiluetaDosDia(): Pokemon ?{
        val pokemonDia = obtenerPokemonDelDia()
        val habilidadDia = obtenerHabilidadPokemonDelDia()
        val statsDia = obtenerStatsPokemonDelDia()
        val lista = obtenerTodosLosPokemon()
        val listaFiltrada = lista.filter { it != pokemonDia && it != habilidadDia && it != statsDia}
        val diaDelAnio = LocalDate.now().dayOfYear
        val random = Random(diaDelAnio.toLong() + 4000)
        val indice = random.nextInt(listaFiltrada.size)
        return listaFiltrada[indice]
    }


    suspend fun syncPokemon() {
        val snapshot = collection.get().await()
        val pokemonsFirebase = snapshot.toObjects(Pokemon::class.java)

        //Convertir a entidades locales
        val entities = pokemonsFirebase.mapNotNull { p ->
            if (p.Nombre.isNotBlank()) PokemonEntity(nombre = p.Nombre) else null
        }
        //Guardar en Room
        dao.insertAll(entities)
    }

    //Buscar en Room (autocompletador)
    suspend fun searchPokemon(query: String): List<PokemonEntity> {
        return dao.searchPokemon(query)
    }

    //Verificar si la base local está vacía
    suspend fun isEmpty(): Boolean {
        return dao.count() == 0
    }
}