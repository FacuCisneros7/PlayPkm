package com.electrofire.playpkm.ui.ViewModels.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.GameAttempts
import com.electrofire.playpkm.Data.Repository.TimeRepository
import com.electrofire.playpkm.Data.UserData
import com.electrofire.playpkm.Data.WeeklyRewardData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar


class HomeStatsViewModel : ViewModel() {

    var userData by mutableStateOf(UserData())
        private set

    var isUserLoaded by mutableStateOf(false)
        private set

    var gameAttempts by mutableStateOf<GameAttempts?>(null)
        private set

    var rachaIncrementadaHoy by mutableStateOf(false)
        private set

    var weeklyRewardToShow by mutableStateOf<WeeklyRewardData?>(null)
        private set

    var isCheckingRewards by mutableStateOf(true)
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
        userData = userData.copy(
            victorias = userData.victorias + 1,
            weeklyWins = userData.weeklyWins + 1
        )
        // Usamos update para que solo se escriban estos campos y no todo el objeto
        actualizarCamposFiresore(mapOf(
            "victorias" to userData.victorias,
            "weeklyWins" to userData.weeklyWins
        ))
    }

    fun registrarDerrota() {
        userData = userData.copy(
            derrotas = userData.derrotas + 1,
            weeklyLosses = userData.weeklyLosses + 1
        )
        actualizarCamposFiresore(mapOf(
            "derrotas" to userData.derrotas,
            "weeklyLosses" to userData.weeklyLosses
        ))
    }

    fun registrarUserName(userName: String) {
        userData = userData.copy(userName = userName)
        actualizarCamposFiresore(mapOf("userName" to userName))
    }

    fun registrarFoto(image: String) {
        userData = userData.copy(imagen = image)
        actualizarCamposFiresore(mapOf("imagen" to image))
    }

    fun registrarInstagram(ig: String) {
        userData = userData.copy(instagram = ig)
        actualizarCamposFiresore(mapOf("instagram" to ig))
    }

    fun registrarNationality(nationality: String) {
        userData = userData.copy(nationality = nationality)
        actualizarCamposFiresore(mapOf("nationality" to nationality))
    }

    fun completarTutorial() {
        userData = userData.copy(hasSeenTutorial = true)
        actualizarCamposFiresore(mapOf("hasSeenTutorial" to true))
    }

    fun registrarCompra(newInsignia: String, cost: Int) {
        if (userData.coins >= cost) {
            val updatedProfileImages = userData.profileImages.toMutableList().apply {
                if (!contains(newInsignia)) add(newInsignia)
            }
            userData = userData.copy(
                coins = userData.coins - cost,
                profileImages = updatedProfileImages
            )
            actualizarCamposFiresore(mapOf(
                "coins" to userData.coins,
                "profileImages" to userData.profileImages
            ))
        }
    }

    private fun verificarReinicioSemanal() {
        timeRepository.obtenerHoraServidorDos { serverDate ->
            val dateToUse = serverDate ?: java.util.Date()

            val calendar = Calendar.getInstance()
            calendar.time = dateToUse
            val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
            val currentYear = calendar.get(Calendar.YEAR)
            val currentWeekId = currentYear * 100 + currentWeek

            val lastWeek = userData.lastWeekParticipated ?: 0
            
            if (lastWeek != 0 && lastWeek != currentWeekId) {
                val reward = userData.weeklyWins * 3
                
                weeklyRewardToShow = WeeklyRewardData(
                    wins = userData.weeklyWins,
                    losses = userData.weeklyLosses,
                    coinsEarned = reward
                )

                userData = userData.copy(
                    weeklyWins = 0,
                    weeklyLosses = 0,
                    coins = userData.coins + reward,
                    lastWeekParticipated = currentWeekId
                )
                guardarStatsCompletos() // Aquí sí guardamos todo el objeto para asegurar consistencia
            } else if (lastWeek == 0) {
                userData = userData.copy(lastWeekParticipated = currentWeekId)
                actualizarCamposFiresore(mapOf("lastWeekParticipated" to currentWeekId))
            }
            isCheckingRewards = false
        }
    }

    private fun verificarYActualizarRacha() {
        timeRepository.obtenerHoraServidorDos { serverDate ->
            if (serverDate == null) return@obtenerHoraServidorDos

            val nowUTC = serverDate.toInstant().atZone(ZoneOffset.UTC).toLocalDate()
            val lastDate = userData.ultimaConexionRacha?.toDate()?.toInstant()?.atZone(ZoneOffset.UTC)?.toLocalDate()

            when {
                lastDate == null -> {
                    userData = userData.copy(rachaActual = 1, ultimaConexionRacha = com.google.firebase.Timestamp(serverDate))
                    rachaIncrementadaHoy = true
                    actualizarCamposFiresore(mapOf(
                        "rachaActual" to 1,
                        "ultimaConexionRacha" to userData.ultimaConexionRacha
                    ))
                }
                lastDate == nowUTC -> {
                    rachaIncrementadaHoy = false
                }
                lastDate.plusDays(1) == nowUTC -> {
                    userData = userData.copy(rachaActual = userData.rachaActual + 1, ultimaConexionRacha = com.google.firebase.Timestamp(serverDate))
                    rachaIncrementadaHoy = true
                    actualizarCamposFiresore(mapOf(
                        "rachaActual" to userData.rachaActual,
                        "ultimaConexionRacha" to userData.ultimaConexionRacha
                    ))
                }
                else -> {
                    userData = userData.copy(rachaActual = 1, ultimaConexionRacha = com.google.firebase.Timestamp(serverDate))
                    rachaIncrementadaHoy = true
                    actualizarCamposFiresore(mapOf(
                        "rachaActual" to 1,
                        "ultimaConexionRacha" to userData.ultimaConexionRacha
                    ))
                }
            }
        }
    }

    // NUEVA FUNCIÓN: Solo actualiza lo que necesitamos, ahorrando escrituras internas
    private fun actualizarCamposFiresore(campos: Map<String, Any?>) {
        userId?.let { uid ->
            firestore.collection("Users").document(uid)
                .update(campos)
                .addOnFailureListener {
                    // Si falla el update (porque el doc no existe), usamos set
                    guardarStatsCompletos()
                }
        }
    }

    private fun guardarStatsCompletos() {
        userId?.let { uid ->
            firestore.collection("Users").document(uid)
                .set(userData, SetOptions.merge())
                .addOnSuccessListener { Log.d("Firestore", "Stats completos guardados") }
        }
    }

    fun cargarStats() {
        userId?.let { uid ->
            firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userData = document.toObject(UserData::class.java) ?: UserData()
                        verificarYActualizarRacha()
                        verificarReinicioSemanal()
                    }
                    isUserLoaded = true
                }

            firestore.collection("attempts").document(uid).get()
                .addOnSuccessListener { document ->
                    gameAttempts = document.toObject(GameAttempts::class.java) ?: GameAttempts()
                }
        } ?: run {
            userData = UserData()
            isUserLoaded = true
            isCheckingRewards = false
        }
    }

    fun verificarAccesoJuego(gameId: String, onResult: (Boolean) -> Unit) {
        val lastTimestamp = when (gameId) {
            "first_game" -> gameAttempts?.first_game
            "second_game" -> gameAttempts?.second_game
            "third_game" -> gameAttempts?.third_game
            "fourth_game" -> gameAttempts?.fourth_game
            "sixth_game" -> gameAttempts?.sixth_game
            "seventh_game" -> gameAttempts?.seventh_game
            "fift_game" -> gameAttempts?.fift_game
            "eight_game" -> gameAttempts?.eight_game
            "ten_game" -> gameAttempts?.ten_game
            "thirteen_game" -> gameAttempts?.thirteen_game
            "forteen_game" -> gameAttempts?.forteen_game
            "fifteen_game" -> gameAttempts?.fifteen_game
            else -> null
        }

        if (lastTimestamp == null) {
            onResult(true)
        } else {
            timeRepository.obtenerHoraServidorDos { horaServidor ->
                val nowUTC = (horaServidor ?: java.util.Date()).toInstant().atZone(ZoneOffset.UTC).toLocalDate()
                val lastDayUTC = lastTimestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate()
                onResult(lastDayUTC != nowUTC)
            }
        }
    }

    fun canPlayGame(gameId: String): Boolean {
        val lastTimestamp = when (gameId) {
            "first_game" -> gameAttempts?.first_game
            "second_game" -> gameAttempts?.second_game
            "third_game" -> gameAttempts?.third_game
            "fourth_game" -> gameAttempts?.fourth_game
            "sixth_game" -> gameAttempts?.sixth_game
            "seventh_game" -> gameAttempts?.seventh_game
            "fift_game" -> gameAttempts?.fift_game
            "eight_game" -> gameAttempts?.eight_game
            "ten_game" -> gameAttempts?.ten_game
            "thirteen_game" -> gameAttempts?.thirteen_game
            "forteen_game" -> gameAttempts?.forteen_game
            "fifteen_game" -> gameAttempts?.fifteen_game
            else -> null
        } ?: return true

        val lastDay = lastTimestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate()
        val nowDay = LocalDate.now(ZoneOffset.UTC)
        return lastDay != nowDay
    }

    fun registrarIntentoJuego(gameId: String) {
        userId?.let { uid ->
            val timestamp = com.google.firebase.Timestamp.now()
            
            // 1. Actualizar localmente de inmediato para que la UI responda sin esperar al servidor
            actualizarIntentoLocal(gameId, timestamp)
            
            // 2. Guardar en Firestore SIN llamar a cargarStats() al éxito
            firestore.collection("attempts").document(uid)
                .set(mapOf(gameId to FieldValue.serverTimestamp()), SetOptions.merge())
                .addOnFailureListener { Log.e("Firestore", "Error al registrar intento") }
        }
    }

    private fun actualizarIntentoLocal(gameId: String, timestamp: com.google.firebase.Timestamp) {
        gameAttempts = when (gameId) {
            "first_game" -> gameAttempts?.copy(first_game = timestamp)
            "second_game" -> gameAttempts?.copy(second_game = timestamp)
            "third_game" -> gameAttempts?.copy(third_game = timestamp)
            "fourth_game" -> gameAttempts?.copy(fourth_game = timestamp)
            "sixth_game" -> gameAttempts?.copy(sixth_game = timestamp)
            "seventh_game" -> gameAttempts?.copy(seventh_game = timestamp)
            "fift_game" -> gameAttempts?.copy(fift_game = timestamp)
            "eight_game" -> gameAttempts?.copy(eight_game = timestamp)
            "ten_game" -> gameAttempts?.copy(ten_game = timestamp)
            "thirteen_game" -> gameAttempts?.copy(thirteen_game = timestamp)
            "forteen_game" -> gameAttempts?.copy(forteen_game = timestamp)
            "fifteen_game" -> gameAttempts?.copy(fifteen_game = timestamp)
            else -> gameAttempts
        }
    }

    fun registrarMaxScoreNinthGame(score: Int) {
        if (score > userData.maxPoints) {
            userData = userData.copy(maxPoints = score)
            actualizarCamposFiresore(mapOf("maxPoints" to score))
        }
    }

    fun registrarMaxScoreElevenGame(score: Int) {
        if (score > userData.maxPointsDos) {
            userData = userData.copy(maxPointsDos = score)
            actualizarCamposFiresore(mapOf("maxPointsDos" to score))
        }
    }

    fun registrarMaxScoreTwelveGame(score: Int) {
        if (score > userData.maxPointsTres) {
            userData = userData.copy(maxPointsTres = score)
            actualizarCamposFiresore(mapOf("maxPointsTres" to score))
        }
    }

    fun dismissWeeklyReward() {
        weeklyRewardToShow = null
    }

    fun reset() {
        userData = UserData()
        gameAttempts = null
        isUserLoaded = false
    }
}
