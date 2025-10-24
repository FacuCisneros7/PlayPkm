package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.Movimiento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

class MovimientosRepository @Inject constructor(){

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Movimientos")

    suspend fun obtenerTodosLosMovimientos(): List<Movimiento>{
        val snapshot = collection.get().await()  //Snapshot contiene_todo lo devuelto en la coleccion
        return snapshot.toObjects(Movimiento::class.java)
    }

    suspend fun obtenerMovimientoDelDia(): Movimiento ?{
        val lista = obtenerTodosLosMovimientos()
        val diaDelAnio = LocalDate.now().dayOfYear
        val random = Random(diaDelAnio.toLong())
        val indice = random.nextInt(lista.size)
        return lista[indice]
    }

}