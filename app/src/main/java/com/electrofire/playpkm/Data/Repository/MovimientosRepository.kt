package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.Movimiento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

class MovimientosRepository @Inject constructor() {
    private val timeRepository = TimeRepository()
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Movimientos")

    private val TOTAL_MOVIMIENTOS = 70

    suspend fun obtenerMovimientoDelDia(): Movimiento? {

        val horaServidor = timeRepository.obtenerHoraServidor() ?: return null
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        val indice = diaDelAnio % TOTAL_MOVIMIENTOS

        val snapshot = collection
            .whereEqualTo("id", indice)
            .limit(1)
            .get()
            .await()

        return snapshot.documents.firstOrNull()
            ?.toObject(Movimiento::class.java)
    }

}
