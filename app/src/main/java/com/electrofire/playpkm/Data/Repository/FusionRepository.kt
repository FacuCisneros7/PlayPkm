package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.Fusion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

class FusionRepository @Inject constructor(){

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Fusion")

    suspend fun obtenerTodasLasFusiones(): List<Fusion>{
        val snapshot = collection.get().await()  //Snapshot contiene_todo lo devuelto en la coleccion
        return snapshot.toObjects(Fusion::class.java)
    }

    suspend fun obtenerFusionDelDia(): Fusion ?{
        val lista = obtenerTodasLasFusiones()
        val diaDelAnio = LocalDate.now().dayOfYear
        val random = Random(diaDelAnio.toLong())
        val indice = random.nextInt(lista.size)
        return lista[indice]
    }

}