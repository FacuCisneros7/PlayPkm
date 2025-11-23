package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.Carta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import kotlin.random.Random

class CartasRepository @Inject constructor(
) {
    private val timeRepository = TimeRepository()

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Cartas")

    suspend fun obtenerTodasLasCartas(): List<Carta> {
        val snapshot =
            collection.get().await()  //Snapshot contiene_todo lo devuelto en la coleccion
        return snapshot.toObjects(Carta::class.java)
    }

    suspend fun obtenerCartaDelDia(): Carta? {
        val lista = obtenerTodasLasCartas()

        val horaServidor = timeRepository.obtenerHoraServidor()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor!!  // ✅ horaServidor es no-null aquí
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        val random = Random(diaDelAnio.toLong())
        val indice = random.nextInt(lista.size)
        return lista[indice]
    }

}