package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.LocalData.PokemonDao
import com.electrofire.playpkm.Data.Pokemon
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import kotlin.random.Random

class PokemonRepository @Inject constructor(
    private val dao: PokemonDao
) {
    private val timeRepository = TimeRepository()
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Pokemon")

    suspend fun obtenerTodosLosPokemon(): List<Pokemon> {
        val snapshot =
            collection.get().await()  //Snapshot contiene_todo lo devuelto en la coleccion
        return snapshot.toObjects(Pokemon::class.java)
    }

    suspend fun obtenerStatsPokemonDelDia(): Pokemon? {
        val lista = obtenerTodosLosPokemon()

        val horaServidor = timeRepository.obtenerHoraServidor()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor!!  // ✅ horaServidor es no-null aquí
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        val random = Random(diaDelAnio.toLong())
        return lista[random.nextInt(lista.size)]

    }

    /*
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
     */

}