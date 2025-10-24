package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.GameAttempts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.Locale

class GameAttemptsRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun canPlayToday(gameId: String, callback: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = firestore.collection("attempts").document(uid)

        // Obtener datos del servidor
        docRef.get(Source.SERVER).addOnSuccessListener { doc ->
            val attempts = doc.toObject(GameAttempts::class.java) ?: GameAttempts()

            val lastTimestamp = when(gameId) {
                "first_game" -> attempts.first_game
                "second_game" -> attempts.second_game
                "third_game" -> attempts.third_game
                "fourth_game" -> attempts.fourth_game
                "sixth_game" -> attempts.sixth_game
                "seventh_game" -> attempts.seventh_game
                "fift_game" -> attempts.fift_game
                else -> null
            }

            val canPlay = if (lastTimestamp != null) {
                val lastDayUTC = lastTimestamp.toDate()
                    .toInstant().atZone(ZoneOffset.UTC).toLocalDate()

                val nowUTC = Timestamp.now().toDate() // ⚠ reemplazaremos con lectura del servidor
                    .toInstant().atZone(ZoneOffset.UTC).toLocalDate()

                lastDayUTC != nowUTC
            } else true

            if (canPlay) {
                val updateMap = when(gameId) {
                    "first_game" -> mapOf("first_game" to FieldValue.serverTimestamp())
                    "second_game" -> mapOf("second_game" to FieldValue.serverTimestamp())
                    "third_game" -> mapOf("third_game" to FieldValue.serverTimestamp())
                    "fourth_game" -> mapOf("fourth_game" to FieldValue.serverTimestamp())
                    "sixth_game" -> mapOf("sixth_game" to FieldValue.serverTimestamp())
                    "seventh_game" -> mapOf("seventh_game" to FieldValue.serverTimestamp())
                    "fift_game" -> mapOf("fift_game" to FieldValue.serverTimestamp())
                    else -> emptyMap()
                }
                if (updateMap.isNotEmpty()) docRef.set(updateMap, SetOptions.merge())
            }

            callback(canPlay)
        }
            .addOnFailureListener {
                // Documento no existe: crear uno nuevo con server timestamp
                val newAttempts = GameAttempts()
                val updateMap = when(gameId) {
                    "first_game" -> mapOf("first_game" to FieldValue.serverTimestamp())
                    "second_game" -> mapOf("second_game" to FieldValue.serverTimestamp())
                    "third_game" -> mapOf("third_game" to FieldValue.serverTimestamp())
                    "fourth_game" -> mapOf("fourth_game" to FieldValue.serverTimestamp())
                    "sixth_game" -> mapOf("sixth_game" to FieldValue.serverTimestamp())
                    "seventh_game" -> mapOf("seventh_game" to FieldValue.serverTimestamp())
                    "fift_game" -> mapOf("fift_game" to FieldValue.serverTimestamp())
                    else -> emptyMap()
                }
                docRef.set(newAttempts).addOnSuccessListener { docRef.set(updateMap, SetOptions.merge()) }
                callback(true)
            }
    }

    fun canPlayTodayDos(gameId: String, callback: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        val docRef = firestore.collection("attempts").document(uid)

        docRef.get()
            .addOnSuccessListener { doc ->
                val attempts = doc.toObject(GameAttempts::class.java) ?: GameAttempts()

                // obtener último intento según el juego
                val lastTimestamp = when(gameId) {
                    "first_game" -> attempts.first_game
                    "second_game" -> attempts.second_game
                    "third_game" -> attempts.third_game
                    "fourth_game" -> attempts.fourth_game
                    "sixth_game" -> attempts.sixth_game
                    "seventh_game" -> attempts.seventh_game
                    "fift_game" -> attempts.fift_game
                    else -> null
                }

                val canPlay = if (lastTimestamp != null) {
                    // calcular la diferencia de días entre timestamps de servidor
                    val lastDay = lastTimestamp.toDate().time / (24*60*60*1000)
                    val todayDay = Timestamp.now().toDate().time / (24*60*60*1000)
                    lastDay != todayDay
                } else {
                    true
                }

                if (canPlay) {
                    // actualizar solo el juego que corresponde con server timestamp
                    val updateMap = when(gameId) {
                        "first_game" -> mapOf("first_game" to FieldValue.serverTimestamp())
                        "second_game" -> mapOf("second_game" to FieldValue.serverTimestamp())
                        "third_game" -> mapOf("third_game" to FieldValue.serverTimestamp())
                        "fourth_game" -> mapOf("fourth_game" to FieldValue.serverTimestamp())
                        "sixth_game" -> mapOf("sixth_game" to FieldValue.serverTimestamp())
                        "seventh_game" -> mapOf("seventh_game" to FieldValue.serverTimestamp())
                        "fift_game" -> mapOf("fift_game" to FieldValue.serverTimestamp())
                        else -> emptyMap()
                    }

                    if (updateMap.isNotEmpty()) {
                        docRef.set(updateMap, SetOptions.merge())
                    }
                }
                callback(canPlay)
            }
            .addOnFailureListener {
                val newAttempts = GameAttempts()
                when(gameId) {
                    "first_game" -> newAttempts.first_game = null
                    "second_game" -> newAttempts.second_game = null
                    "third_game" -> newAttempts.third_game = null
                    "fourth_game" -> newAttempts.fourth_game = null
                    "sixth_game" -> newAttempts.sixth_game = null
                    "seventh_game" -> newAttempts.seventh_game = null
                    "fift_game" -> newAttempts.fift_game = null
                }
                docRef.set(newAttempts)
                    .addOnSuccessListener {
                        // ahora actualizamos con server timestamp
                        val updateMap = when(gameId) {
                            "first_game" -> mapOf("first_game" to FieldValue.serverTimestamp())
                            "second_game" -> mapOf("second_game" to FieldValue.serverTimestamp())
                            "third_game" -> mapOf("third_game" to FieldValue.serverTimestamp())
                            "fourth_game" -> mapOf("fourth_game" to FieldValue.serverTimestamp())
                            "sixth_game" -> mapOf("sixth_game" to FieldValue.serverTimestamp())
                            "seventh_game" -> mapOf("seventh_game" to FieldValue.serverTimestamp())
                            "fift_game" -> mapOf("fift_game" to FieldValue.serverTimestamp())
                            else -> emptyMap()
                        }
                        if (updateMap.isNotEmpty()) docRef.set(updateMap, SetOptions.merge())
                        callback(true)
                    }
            }
    }

}