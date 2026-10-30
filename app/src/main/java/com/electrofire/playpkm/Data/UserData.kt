package com.electrofire.playpkm.Data

data class UserData(
    val victorias: Int = 0,
    val derrotas: Int = 0,
    var userName: String? = null,
    var avatarId: String = "0025",
    var purchasedAvatars: List<String> = listOf("0025"),
    var id: String? = null,
    val maxPoints: Int = 0,
    val maxPointsTres: Int = 0,
    val maxPointsDos: Int = 0,
    val instagram: String? = null,
    val rachaActual: Int = 0,
    val ultimaConexionRacha: com.google.firebase.Timestamp? = null,
    val nationality: String? = null,
    val hasSeenTutorial: Boolean = false,
    val coins: Int = 0,
    val weeklyWins: Int = 0,
    val weeklyLosses: Int = 0,
    val lastWeekParticipated: Int? = null,
    val missions: List<Mission> = emptyList(),
    val lastMissionsResetDate: com.google.firebase.Timestamp? = null,
    val lastWeeklyMissionsResetDate: com.google.firebase.Timestamp? = null
)
