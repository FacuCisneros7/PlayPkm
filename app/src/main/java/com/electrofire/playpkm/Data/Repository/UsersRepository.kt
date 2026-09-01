package com.electrofire.playpkm.Data.Repository

import android.util.Log
import com.electrofire.playpkm.Data.RankingCache
import com.electrofire.playpkm.Data.UserData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

class UsersRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    // Función auxiliar para obtener el ID de la semana actual (Ej: 202636)
    private fun getCurrentWeekId(): Int {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val year = calendar.get(Calendar.YEAR)
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        return year * 100 + week
    }

    suspend fun getRankingCache(): RankingCache? {
        return try {
            val doc = db.collection("rankings_cache").document("global")
                .get()
                .await()
            
            if (doc.exists()) {
                val cache = doc.toObject(RankingCache::class.java)
                Log.d("RankingCache", "Caché recuperado con éxito. LastUpdated: ${cache?.lastUpdated}")
                cache
            } else {
                Log.d("RankingCache", "El documento de caché no existe en Firestore")
                null
            }
        } catch (e: Exception) {
            Log.e("RankingCache", "Error al leer el caché", e)
            null
        }
    }

    suspend fun refreshAndSaveRankingCache(): RankingCache {
        Log.d("RankingCache", "Iniciando refresco masivo de rankings (120 lecturas...)")
        val weekly = getUsersOrderedByWeeklyWins()
        val gc = getUsersOrderedByVictoriesInGC()
        val ts = getUsersOrderedByVictoriesInTS()
        val ba = getUsersOrderedByVictoriesInBA()

        val docRef = db.collection("rankings_cache").document("global")
        
        val data = mapOf(
            "weekly" to weekly,
            "goodChoice" to gc,
            "thousandShadows" to ts,
            "beforeAfter" to ba,
            "lastUpdated" to FieldValue.serverTimestamp()
        )

        try {
            docRef.set(data).await()
            Log.d("RankingCache", "Nuevo caché guardado atómicamente en Firestore")
        } catch (e: Exception) {
            Log.e("RankingCache", "Error al guardar el nuevo caché", e)
        }

        return RankingCache(Timestamp.now(), weekly, gc, ts, ba)
    }

    suspend fun getUsersOrderedByWeeklyWins(): List<UserData> {
        Log.e("RANKING_CRITICO", "ALERTA: Se ha llamado a getUsersOrderedByWeeklyWins() - Esto gasta 30 lecturas!")
        return try {
            val currentWeekId = getCurrentWeekId()
            
            val snapshot = db.collection("Users")
                .whereEqualTo("lastWeekParticipated", currentWeekId) // FILTRO CLAVE: Solo gente de esta semana activa
                .orderBy("weeklyWins", Query.Direction.DESCENDING)
                .orderBy("weeklyLosses", Query.Direction.ASCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("RANKING_ERROR", "Error en query semanal: ${e.message}")
            emptyList()
        }
    }

    suspend fun getUsersOrderedByVictoriesInGC(): List<UserData> {
        Log.e("RANKING_CRITICO", "ALERTA: Se ha llamado a getUsersOrderedByVictoriesInGC() - Esto gasta 30 lecturas!")
        return try {
            val snapshot = db.collection("Users")
                .orderBy("maxPoints", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUsersOrderedByVictoriesInTS(): List<UserData> {
        Log.e("RANKING_CRITICO", "ALERTA: Se ha llamado a getUsersOrderedByVictoriesInTS() - Esto gasta 30 lecturas!")
        return try {
            val snapshot = db.collection("Users")
                .orderBy("maxPointsDos", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUsersOrderedByVictoriesInBA(): List<UserData> {
        Log.e("RANKING_CRITICO", "ALERTA: Se ha llamado a getUsersOrderedByVictoriesInBA() - Esto gasta 30 lecturas!")
        return try {
            val snapshot = db.collection("Users")
                .orderBy("maxPointsTres", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
