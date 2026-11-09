package com.electrofire.playpkm.ui.ViewModels.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.GameAttempts
import com.electrofire.playpkm.Data.Mission
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

    var isAttemptsLoaded by mutableStateOf(false)
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
        incrementarProgresoMision("daily_5_wins")
        incrementarProgresoMision("weekly_25_wins")

        actualizarCamposFiresore(
            mapOf(
                "victorias" to userData.victorias,
                "weeklyWins" to userData.weeklyWins,
                "missions" to userData.missions
            )
        )
    }

    fun registrarDerrota() {
        userData = userData.copy(
            derrotas = userData.derrotas + 1,
            weeklyLosses = userData.weeklyLosses + 1
        )
        actualizarCamposFiresore(
            mapOf(
                "derrotas" to userData.derrotas,
                "weeklyLosses" to userData.weeklyLosses
            )
        )
    }

    fun registrarUserName(userName: String) {
        userData = userData.copy(userName = userName)
        actualizarCamposFiresore(mapOf("userName" to userName))
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

    fun registrarCompra(avatarId: String, cost: Int) {
        if (userData.coins >= cost) {
            val updatedPurchased = userData.purchasedAvatars.toMutableList().apply {
                if (!contains(avatarId)) add(avatarId)
            }
            userData = userData.copy(
                coins = userData.coins - cost,
                purchasedAvatars = updatedPurchased,
                avatarId = avatarId
            )
            actualizarCamposFiresore(
                mapOf(
                    "coins" to userData.coins,
                    "purchasedAvatars" to userData.purchasedAvatars,
                    "avatarId" to userData.avatarId
                )
            )
        }
    }

    fun seleccionarAvatar(avatarId: String) {
        val updatedPurchased = userData.purchasedAvatars.toMutableList().apply {
            if (!contains(avatarId)) add(avatarId)
        }
        userData = userData.copy(
            avatarId = avatarId,
            purchasedAvatars = updatedPurchased
        )
        actualizarCamposFiresore(
            mapOf(
                "avatarId" to avatarId,
                "purchasedAvatars" to userData.purchasedAvatars
            )
        )
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
                guardarStatsCompletos()
            } else if (lastWeek == 0) {
                userData = userData.copy(lastWeekParticipated = currentWeekId)
                actualizarCamposFiresore(mapOf("lastWeekParticipated" to currentWeekId))
            }

            verificarYInicializarMisiones(dateToUse)
            isCheckingRewards = false
        }
    }

    private fun verificarYInicializarMisiones(serverDate: java.util.Date) {
        val calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        calendar.time = serverDate
        val currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)

        var currentMissions = userData.missions.toMutableList()

        if (currentMissions.isEmpty()) {
            val defaultDaily = listOf(
                Mission("daily_15_games", "Juega los 15 juegos diarios", 0, 15, 5, false, "DAILY"),
                Mission("daily_5_wins", "Gana 5 juegos diarios", 0, 5, 3, false, "DAILY"),
                Mission("daily_3_free", "Juega 3 veces un juego libre", 0, 3, 2, false, "DAILY")
            )
            val defaultWeekly = listOf(
                Mission(
                    "weekly_50_games",
                    "Juega 50 veces juegos diarios",
                    0,
                    50,
                    20,
                    false,
                    "WEEKLY"
                ),
                Mission("weekly_30_wins", "Consigue 30 victorias", 0, 30, 30, false, "WEEKLY")
            )
            currentMissions = (defaultDaily + defaultWeekly).toMutableList()
            userData = userData.copy(
                missions = currentMissions,
                lastMissionsResetDate = userData.lastMissionsResetDate
                    ?: com.google.firebase.Timestamp(serverDate),
                lastWeeklyMissionsResetDate = userData.lastWeeklyMissionsResetDate
                    ?: com.google.firebase.Timestamp(serverDate)
            )
            guardarStatsCompletos()
            return
        }

        val lastDailyDay = userData.lastMissionsResetDate?.toDate()?.let {
            val c = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
            c.time = it
            c.get(Calendar.DAY_OF_YEAR)
        }

        val lastDailyYear = userData.lastMissionsResetDate?.toDate()?.let {
            val c = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
            c.time = it
            c.get(Calendar.YEAR)
        }

        val lastWeeklyWeek = userData.lastWeeklyMissionsResetDate?.toDate()?.let {
            val c = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
            c.time = it
            c.get(Calendar.WEEK_OF_YEAR)
        }

        val needsDailyReset =
            lastDailyDay != null && (lastDailyDay != currentDayOfYear || lastDailyYear != currentYear)
        val needsWeeklyReset = lastWeeklyWeek != null && lastWeeklyWeek != currentWeekOfYear

        if (needsDailyReset || needsWeeklyReset) {
            val defaultDaily = listOf(
                Mission("daily_15_games", "Juega los 15 juegos diarios", 0, 15, 5, false, "DAILY"),
                Mission("daily_5_wins", "Gana 5 juegos diarios", 0, 5, 3, false, "DAILY"),
                Mission("daily_3_free", "Juega 3 veces un juego libre", 0, 3, 2, false, "DAILY")
            )
            val defaultWeekly = listOf(
                Mission(
                    "weekly_50_games",
                    "Juega 50 veces juegos diarios",
                    0,
                    50,
                    20,
                    false,
                    "WEEKLY"
                ),
                Mission("weekly_30_wins", "Consigue 30 victorias", 0, 30, 30, false, "WEEKLY")
            )

            if (needsDailyReset) {
                currentMissions.removeAll { it.type == "DAILY" }
                currentMissions.addAll(defaultDaily)
            }
            if (needsWeeklyReset) {
                currentMissions.removeAll { it.type == "WEEKLY" }
                currentMissions.addAll(defaultWeekly)
            }

            userData = userData.copy(
                missions = currentMissions,
                lastMissionsResetDate = if (needsDailyReset) com.google.firebase.Timestamp(
                    serverDate
                ) else userData.lastMissionsResetDate,
                lastWeeklyMissionsResetDate = if (needsWeeklyReset) com.google.firebase.Timestamp(
                    serverDate
                ) else userData.lastWeeklyMissionsResetDate
            )
            guardarStatsCompletos()
        }
    }

    fun incrementarProgresoMision(missionId: String, amount: Int = 1) {
        val updatedMissions = userData.missions.map { m ->
            if (m.id == missionId && !m.isClaimed) {
                val newProg = (m.currentProgress + amount).coerceAtMost(m.targetProgress)
                m.copy(currentProgress = newProg)
            } else m
        }
        if (updatedMissions != userData.missions) {
            userData = userData.copy(missions = updatedMissions)
            actualizarCamposFiresore(mapOf("missions" to userData.missions))
        }
    }

    fun canjearMision(missionId: String) {
        val mission = userData.missions.find { it.id == missionId }
        if (mission != null && !mission.isClaimed && mission.currentProgress >= mission.targetProgress) {
            val updatedMissions = userData.missions.map { m ->
                if (m.id == missionId) m.copy(isClaimed = true) else m
            }
            userData = userData.copy(
                missions = updatedMissions,
                coins = userData.coins + mission.rewardCoins
            )
            actualizarCamposFiresore(
                mapOf(
                    "missions" to userData.missions,
                    "coins" to userData.coins
                )
            )
            guardarStatsCompletos()
        }
    }

    fun registrarJuegoLibre() {
        incrementarProgresoMision("daily_3_free")
    }

    private fun verificarYActualizarRacha() {
        timeRepository.obtenerHoraServidorDos { serverDate ->
            if (serverDate == null) return@obtenerHoraServidorDos

            val nowUTC = serverDate.toInstant().atZone(ZoneOffset.UTC).toLocalDate()
            val lastDate =
                userData.ultimaConexionRacha?.toDate()?.toInstant()?.atZone(ZoneOffset.UTC)
                    ?.toLocalDate()

            when {
                lastDate == null -> {
                    userData = userData.copy(
                        rachaActual = 1,
                        ultimaConexionRacha = com.google.firebase.Timestamp(serverDate)
                    )
                    rachaIncrementadaHoy = true
                    actualizarCamposFiresore(
                        mapOf(
                            "rachaActual" to 1,
                            "ultimaConexionRacha" to userData.ultimaConexionRacha
                        )
                    )
                }

                lastDate == nowUTC -> {
                    rachaIncrementadaHoy = false
                }

                lastDate.plusDays(1) == nowUTC -> {
                    userData = userData.copy(
                        rachaActual = userData.rachaActual + 1,
                        ultimaConexionRacha = com.google.firebase.Timestamp(serverDate)
                    )
                    rachaIncrementadaHoy = true
                    actualizarCamposFiresore(
                        mapOf(
                            "rachaActual" to userData.rachaActual,
                            "ultimaConexionRacha" to userData.ultimaConexionRacha
                        )
                    )
                }

                else -> {
                    userData = userData.copy(
                        rachaActual = 1,
                        ultimaConexionRacha = com.google.firebase.Timestamp(serverDate)
                    )
                    rachaIncrementadaHoy = true
                    actualizarCamposFiresore(
                        mapOf(
                            "rachaActual" to 1,
                            "ultimaConexionRacha" to userData.ultimaConexionRacha
                        )
                    )
                }
            }
        }
    }

    private fun actualizarCamposFiresore(campos: Map<String, Any?>) {
        userId?.let { uid ->
            firestore.collection("Users").document(uid)
                .update(campos)
                .addOnFailureListener {
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
        userData = UserData()
        gameAttempts = null
        isUserLoaded = false
        isCheckingRewards = true

        userId?.let { uid ->
            firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val obj = document.toObject(UserData::class.java) ?: UserData()

                        val rawMissions = document.get("missions") as? List<*>

                        val parsedMissions = rawMissions?.mapNotNull { item ->
                            val map = item as? Map<*, *>
                            if (map != null) {
                                val rawClaimed = map["isClaimed"] ?: map["claimed"]
                                Mission(
                                    id = map["id"] as? String ?: "",
                                    title = map["title"] as? String ?: "",
                                    currentProgress = (map["currentProgress"] as? Long)?.toInt()
                                        ?: (map["currentProgress"] as? Int) ?: 0,
                                    targetProgress = (map["targetProgress"] as? Long)?.toInt()
                                        ?: (map["targetProgress"] as? Int) ?: 1,
                                    rewardCoins = (map["rewardCoins"] as? Long)?.toInt()
                                        ?: (map["rewardCoins"] as? Int) ?: 100,
                                    isClaimed = rawClaimed as? Boolean ?: false,
                                    type = map["type"] as? String ?: "DAILY"
                                )
                            } else null
                        } ?: obj.missions

                        val resetDate = document.getTimestamp("lastMissionsResetDate")
                            ?: obj.lastMissionsResetDate
                        val weeklyResetDate = document.getTimestamp("lastWeeklyMissionsResetDate")
                            ?: obj.lastWeeklyMissionsResetDate

                        userData = obj.copy(
                            missions = parsedMissions,
                            lastMissionsResetDate = resetDate,
                            lastWeeklyMissionsResetDate = weeklyResetDate
                        )
                    } else {
                        userData = UserData()
                    }
                    verificarYActualizarRacha()
                    verificarReinicioSemanal()
                    isUserLoaded = true
                }
                .addOnFailureListener {
                    userData = UserData()
                    verificarYActualizarRacha()
                    verificarReinicioSemanal()
                    isUserLoaded = true
                    isCheckingRewards = false
                }

            firestore.collection("attempts").document(uid).get()
                .addOnSuccessListener { document ->
                    gameAttempts = document.toObject(GameAttempts::class.java) ?: GameAttempts()
                    isAttemptsLoaded = true
                }
                .addOnFailureListener {
                    gameAttempts = GameAttempts()
                    isAttemptsLoaded = true
                }
        } ?: run {
            userData = UserData()
            isAttemptsLoaded = true
            isUserLoaded = true
            isCheckingRewards = false
        }
    }

    fun verificarAccesoJuego(gameId: String, onResult: (Boolean) -> Unit) {
        if (!isAttemptsLoaded) {
            onResult(false)
            return
        }

        val lastTimestamp = when (gameId) {
            "first_game" -> gameAttempts?.first_game
            "second_game" -> gameAttempts?.second_game
            "third_game" -> gameAttempts?.third_game
            "sixth_game" -> gameAttempts?.sixth_game
            "seventh_game" -> gameAttempts?.seventh_game
            "fift_game" -> gameAttempts?.fift_game
            "eight_game" -> gameAttempts?.eight_game
            "ten_game" -> gameAttempts?.ten_game
            "thirteen_game" -> gameAttempts?.thirteen_game
            "forteen_game" -> gameAttempts?.forteen_game
            "fifteen_game" -> gameAttempts?.fifteen_game
            "sixteen_game" -> gameAttempts?.sixteen_game
            "seventeen_game" -> gameAttempts?.seventeen_game
            "eighteen_game" -> gameAttempts?.eighteen_game
            "nineteen_game" -> gameAttempts?.nineteen_game
            else -> null
        }

        if (lastTimestamp == null) {
            onResult(true)
        } else {
            timeRepository.obtenerHoraServidorDos { horaServidor ->
                val nowUTC = (horaServidor ?: java.util.Date()).toInstant().atZone(ZoneOffset.UTC)
                    .toLocalDate()
                val lastDayUTC =
                    lastTimestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate()
                onResult(lastDayUTC != nowUTC)
            }
        }
    }

    fun canPlayGame(gameId: String): Boolean {
        if (!isAttemptsLoaded) return false

        val lastTimestamp = when (gameId) {
            "first_game" -> gameAttempts?.first_game
            "second_game" -> gameAttempts?.second_game
            "third_game" -> gameAttempts?.third_game
            "sixth_game" -> gameAttempts?.sixth_game
            "seventh_game" -> gameAttempts?.seventh_game
            "fift_game" -> gameAttempts?.fift_game
            "eight_game" -> gameAttempts?.eight_game
            "ten_game" -> gameAttempts?.ten_game
            "thirteen_game" -> gameAttempts?.thirteen_game
            "forteen_game" -> gameAttempts?.forteen_game
            "fifteen_game" -> gameAttempts?.fifteen_game
            "sixteen_game" -> gameAttempts?.sixteen_game
            "seventeen_game" -> gameAttempts?.seventeen_game
            "eighteen_game" -> gameAttempts?.eighteen_game
            "nineteen_game" -> gameAttempts?.nineteen_game
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
            incrementarProgresoMision("daily_15_games")
            incrementarProgresoMision("weekly_50_games")

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
            "sixth_game" -> gameAttempts?.copy(sixth_game = timestamp)
            "seventh_game" -> gameAttempts?.copy(seventh_game = timestamp)
            "fift_game" -> gameAttempts?.copy(fift_game = timestamp)
            "eight_game" -> gameAttempts?.copy(eight_game = timestamp)
            "ten_game" -> gameAttempts?.copy(ten_game = timestamp)
            "thirteen_game" -> gameAttempts?.copy(thirteen_game = timestamp)
            "forteen_game" -> gameAttempts?.copy(forteen_game = timestamp)
            "fifteen_game" -> gameAttempts?.copy(fifteen_game = timestamp)
            "sixteen_game" -> gameAttempts?.copy(sixteen_game = timestamp)
            "seventeen_game" -> gameAttempts?.copy(seventeen_game = timestamp)
            "eighteen_game" -> gameAttempts?.copy(eighteen_game = timestamp)
            "nineteen_game" -> gameAttempts?.copy(nineteen_game = timestamp)
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
