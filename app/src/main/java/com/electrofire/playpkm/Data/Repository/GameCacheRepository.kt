package com.electrofire.playpkm.Data.Repository

import android.util.Log
import com.electrofire.playpkm.Data.Carta
import com.electrofire.playpkm.Data.Fusion
import com.electrofire.playpkm.Data.GameCache
import com.electrofire.playpkm.Data.Movimiento
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameCacheRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val timeRepository = TimeRepository()
    private val cacheDoc = db.collection("game_cache").document("daily")

    // CACHÉ EN MEMORIA (RAM)
    private var memoryCache: GameCache? = null
    private val TOTAL_CARTAS = 100
    private val TOTAL_MOVIMIENTOS = 70
    private val TOTAL_FUSIONES = 61

    private suspend fun getGameBundle(): GameCache? {
        // Si ya lo descargamos en esta sesión, lo devolvemos de la RAM al instante
        memoryCache?.let {
            Log.d("GameCache", "Sirviendo bundle desde la RAM (0 lecturas)")
            return it
        }

        return try {
            val snapshot = cacheDoc.get().await()
            if (snapshot.exists()) {
                val bundle = snapshot.toObject(GameCache::class.java)
                memoryCache = bundle // Guardamos en RAM para la próxima vez
                bundle
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("GameCache", "Error leyendo cache de juegos: ${e.message}")
            null
        }
    }

    suspend fun getValidatedBundle(): GameCache? {
        val currentCache = getGameBundle()
        
        val horaServidor = timeRepository.obtenerHoraServidor() ?: return currentCache

        val nowUTC = horaServidor.toInstant().atZone(ZoneOffset.UTC).toLocalDate()
        val lastUpdatedDate = currentCache?.lastUpdated?.toDate()
        
        if (currentCache != null && currentCache.lastUpdated == null) {
            Log.d("GameCache", "Bundle diario obtenido exitosamente")
            return currentCache
        }

        val lastUpdatedUTC = lastUpdatedDate?.toInstant()?.atZone(ZoneOffset.UTC)?.toLocalDate()

        val needsRefresh = currentCache == null || (lastUpdatedUTC != null && lastUpdatedUTC != nowUTC)

        return if (needsRefresh) {
            val newBundle = refreshAndSaveGameBundle()
            memoryCache = newBundle // Actualizamos la RAM con el nuevo bundle diario
            newBundle
        } else {
            currentCache
        }
    }

    private suspend fun refreshAndSaveGameBundle(): GameCache {
        Log.w("GameCache", "--- REFRESCANDO BUNDLE DIARIO DE JUEGOS ---")
        
        val horaServidor = timeRepository.obtenerHoraServidor() ?: Date()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor
        val diaDelAnio = calendar.get(Calendar.DAY_OF_YEAR)

        // 1. Obtener Carta
        val carta = try {
            val idx = diaDelAnio % TOTAL_CARTAS
            db.collection("Cartas").whereEqualTo("id", idx).limit(1).get().await()
                .documents.firstOrNull()?.toObject(Carta::class.java)
        } catch (e: Exception) { null }

        // 2. Obtener Movimiento
        val movimiento = try {
            val idx = diaDelAnio % TOTAL_MOVIMIENTOS
            db.collection("Movimientos").whereEqualTo("id", idx).limit(1).get().await()
                .documents.firstOrNull()?.toObject(Movimiento::class.java)
        } catch (e: Exception) { null }

        // 3. Obtener Fusion
        val fusion = try {
            val idx = diaDelAnio % TOTAL_FUSIONES
            db.collection("Fusion").whereEqualTo("id", idx).limit(1).get().await()
                .documents.firstOrNull()?.toObject(Fusion::class.java)
        } catch (e: Exception) { null }

        val newCache = GameCache(
            carta = carta,
            movimiento = movimiento,
            fusion = fusion,
            lastUpdated = Timestamp(horaServidor)
        )

        try {
            cacheDoc.set(newCache).await()
            Log.d("GameCache", "Bundle diario guardado exitosamente.")
        } catch (e: Exception) {
            Log.e("GameCache", "Error guardando bundle: ${e.message}")
        }

        return newCache
    }
}
