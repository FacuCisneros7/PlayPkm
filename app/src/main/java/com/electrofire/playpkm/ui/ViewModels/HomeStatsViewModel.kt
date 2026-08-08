package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.GameAttempts
import com.electrofire.playpkm.Data.Repository.TimeRepository
import com.electrofire.playpkm.Data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import java.time.ZoneOffset
import java.util.Date
import javax.inject.Inject


class HomeStatsViewModel : ViewModel() {

    var userData by mutableStateOf(UserData())
        private set

    var isUserLoaded by mutableStateOf(false)
        private set

    var gameAttempts by mutableStateOf<GameAttempts?>(null)
        private set

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val timeRepository = TimeRepository()

    private val userId: String?
        get() = auth.currentUser?.uid

    init {
        cargarStats()
    }

    fun registrarVictoria() {
        userData = userData.copy(victorias = userData.victorias + 1)
        guardarStats()
    }

    fun registrarDerrota() {
        userData = userData.copy(derrotas = userData.derrotas + 1)
        guardarStats()
    }

    fun registrarUserName(userName: String) {
        userData = userData.copy(userName = userName)
        guardarStats()
    }

    fun registrarFoto(image: String) {
        userData = userData.copy(imagen = image)
        guardarStats()
    }

    private fun guardarStats() {
        userId?.let { uid ->
            firestore.collection("Users")
                .document(uid)
                .set(
                    mapOf(
                        "userName" to userData.userName,
                        "imagen" to userData.imagen,
                        "victorias" to userData.victorias,
                        "derrotas" to userData.derrotas,
                        "maxPoints" to userData.maxPoints,
                        "maxPointsDos" to userData.maxPointsDos,
                        "maxPointsTres" to userData.maxPointsTres
                    ),
                    SetOptions.merge()
                )
                .addOnSuccessListener {
                    Log.d("Firestore", "Stats guardados correctamente")
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error guardando stats", it)
                }
        }
    }

    fun cargarStats() {
        userId?.let { uid ->
            // Cargar datos de usuario
            firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val imagenActual = document.getString("imagen") ?: ""
                        val profileImages = document.get("profileImages") as? List<String>

                        if (profileImages == null && imagenActual.isNotEmpty()) {
                            firestore.collection("Users").document(uid).update("profileImages", listOf(imagenActual))
                        }

                        userData = UserData(
                            userName = document.getString("userName"),
                            imagen = imagenActual,
                            victorias = document.getLong("victorias")?.toInt() ?: 0,
                            derrotas = document.getLong("derrotas")?.toInt() ?: 0,
                            maxPoints = document.getLong("maxPoints")?.toInt() ?: 0,
                            profileImages = profileImages ?: listOf(imagenActual),
                            maxPointsDos = document.getLong("maxPointsDos")?.toInt() ?: 0,
                            maxPointsTres = document.getLong("maxPointsTres")?.toInt() ?: 0
                        )
                    }
                    isUserLoaded = true
                }

            // Cargar intentos (Task 1) - Sin forzar SERVER para usar caché si está disponible
            firestore.collection("attempts").document(uid).get()
                .addOnSuccessListener { document ->
                    gameAttempts = document.toObject(GameAttempts::class.java) ?: GameAttempts()
                }
        } ?: run {
            userData = UserData()
            isUserLoaded = true
        }
    }

    fun verificarAccesoJuego(gameId: String, onResult: (Boolean) -> Unit) {
        val attempts = gameAttempts ?: run {
            onResult(true)
            return
        }

        val lastTimestamp = when (gameId) {
            "first_game" -> attempts.first_game
            "second_game" -> attempts.second_game
            "third_game" -> attempts.third_game
            "fourth_game" -> attempts.fourth_game
            "sixth_game" -> attempts.sixth_game
            "seventh_game" -> attempts.seventh_game
            "fift_game" -> attempts.fift_game
            "eight_game" -> attempts.eight_game
            "ten_game" -> attempts.ten_game
            else -> null
        }

        if (lastTimestamp == null) {
            onResult(true)
        } else {
            timeRepository.obtenerHoraServidorDos { horaServidor ->
                val nowUTC = horaServidor?.toInstant()?.atZone(ZoneOffset.UTC)?.toLocalDate()
                val lastDayUTC = lastTimestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate()
                onResult(lastDayUTC != nowUTC)
            }
        }
    }

    fun registrarIntentoJuego(gameId: String) {
        val uid = userId ?: return
        val docRef = firestore.collection("attempts").document(uid)
        
        val updateMap = mapOf(gameId to FieldValue.serverTimestamp())
        
        docRef.set(updateMap, SetOptions.merge())
            .addOnSuccessListener {
                // Actualizar localmente para evitar lecturas extras
                // Nota: Esto es una simplificación, en un mundo ideal usaríamos el server timestamp retornado o SnapshotListeners
                cargarStats() 
            }
    }


    fun registrarMaxScoreNinthGame(score: Int) {
        if (score > userData.maxPoints) {
            userData = userData.copy(maxPoints = score)
            guardarStats()
        }
    }

    fun registrarMaxScoreElevenGame(score: Int) {
        if (score > userData.maxPointsDos) {
            userData = userData.copy(maxPointsDos = score)
            guardarStats()
        }
    }

    fun registrarMaxScoreTwelveGame(score: Int) {
        if (score > userData.maxPointsTres) {
            userData = userData.copy(maxPointsTres = score)
            guardarStats()
        }
    }

    fun reset() {
        userData = userData.copy(
            victorias = 0,
            derrotas = 0,
            maxPoints = 0
        )
    }

}