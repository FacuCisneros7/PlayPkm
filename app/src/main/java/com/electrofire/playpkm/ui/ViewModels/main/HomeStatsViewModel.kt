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
        guardarStats()
    }

    fun registrarDerrota() {
        userData = userData.copy(
            derrotas = userData.derrotas + 1,
            weeklyLosses = userData.weeklyLosses + 1
        )
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

    fun registrarInstagram(ig: String) {
        userData = userData.copy(instagram = ig)
        guardarStats()
    }

    fun registrarNationality(nationality: String) {
        userData = userData.copy(nationality = nationality)
        guardarStats()
    }

    fun completarTutorial() {
        userData = userData.copy(hasSeenTutorial = true)
        guardarStats()
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
            guardarStats()
        }
    }

    private fun verificarReinicioSemanal() {
        timeRepository.obtenerHoraServidorDos { serverDate ->
            val dateToUse = serverDate ?: java.util.Date() // Si falla el server, usamos la local como respaldo

            val calendar = Calendar.getInstance()
            calendar.time = dateToUse
            val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
            val currentYear = calendar.get(Calendar.YEAR)
            val currentWeekId = currentYear * 100 + currentWeek

            // Si el lastWeekParticipated es de formato viejo (menor a 200000), forzamos reinicio
            val lastWeek = userData.lastWeekParticipated ?: 0
            
            if (lastWeek != 0 && lastWeek != currentWeekId) {
                // ¡REINICIO SEMANAL DETECTADO!
                val reward = userData.weeklyWins * 3
                Log.d("RESET", "Reiniciando semana. Recompensa: $reward monedas.")
                
                // Guardamos los datos para mostrar el diálogo
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
                guardarStats()
            } else if (lastWeek == 0) {
                // Primer inicio con este sistema
                userData = userData.copy(lastWeekParticipated = currentWeekId)
                guardarStats()
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
                    guardarStats()
                }
                lastDate == nowUTC -> {
                    rachaIncrementadaHoy = false
                }
                lastDate.plusDays(1) == nowUTC -> {
                    userData = userData.copy(rachaActual = userData.rachaActual + 1, ultimaConexionRacha = com.google.firebase.Timestamp(serverDate))
                    rachaIncrementadaHoy = true
                    guardarStats()
                }
                else -> {
                    userData = userData.copy(rachaActual = 1, ultimaConexionRacha = com.google.firebase.Timestamp(serverDate))
                    rachaIncrementadaHoy = true
                    guardarStats()
                }
            }
        }
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
                        "maxPointsTres" to userData.maxPointsTres,
                        "profileImages" to userData.profileImages,
                        "instagram" to userData.instagram,
                        "rachaActual" to userData.rachaActual,
                        "ultimaConexionRacha" to userData.ultimaConexionRacha,
                        "nationality" to userData.nationality,
                        "hasSeenTutorial" to userData.hasSeenTutorial,
                        "coins" to userData.coins,
                        "weeklyWins" to userData.weeklyWins,
                        "weeklyLosses" to userData.weeklyLosses,
                        "lastWeekParticipated" to userData.lastWeekParticipated
                    ),
                    SetOptions.merge()
                )
                .addOnSuccessListener {
                    Log.d("Firestore", "Stats guardados correctamente")
                }
        }
    }

    fun cargarStats() {
        userId?.let { uid ->
            firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val profileImages = document.get("profileImages") as? List<String> ?: emptyList()
                        
                        userData = UserData(
                            userName = document.getString("userName"),
                            imagen = document.getString("imagen") ?: "",
                            victorias = document.getLong("victorias")?.toInt() ?: 0,
                            derrotas = document.getLong("derrotas")?.toInt() ?: 0,
                            maxPoints = document.getLong("maxPoints")?.toInt() ?: 0,
                            profileImages = profileImages,
                            maxPointsDos = document.getLong("maxPointsDos")?.toInt() ?: 0,
                            maxPointsTres = document.getLong("maxPointsTres")?.toInt() ?: 0,
                            instagram = document.getString("instagram"),
                            rachaActual = document.getLong("rachaActual")?.toInt() ?: 0,
                            ultimaConexionRacha = document.getTimestamp("ultimaConexionRacha"),
                            nationality = document.getString("nationality"),
                            hasSeenTutorial = document.getBoolean("hasSeenTutorial") ?: false,
                            coins = document.getLong("coins")?.toInt() ?: 0,
                            weeklyWins = document.getLong("weeklyWins")?.toInt() ?: 0,
                            weeklyLosses = document.getLong("weeklyLosses")?.toInt() ?: 0,
                            lastWeekParticipated = document.getLong("lastWeekParticipated")?.toInt()
                        )
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
            else -> null
        } ?: return true

        val lastDay = lastTimestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate()
        val nowDay = LocalDate.now(ZoneOffset.UTC)
        return lastDay != nowDay
    }

    fun registrarIntentoJuego(gameId: String) {
        userId?.let { uid ->
            firestore.collection("attempts").document(uid)
                .set(mapOf(gameId to FieldValue.serverTimestamp()), SetOptions.merge())
                .addOnSuccessListener { cargarStats() }
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

    fun dismissWeeklyReward() {
        weeklyRewardToShow = null
    }

    fun reset() {
        userData = UserData()
        gameAttempts = null
        isUserLoaded = false
    }
}
