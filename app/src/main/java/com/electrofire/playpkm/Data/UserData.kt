package com.electrofire.playpkm.Data

data class UserData(
    val victorias: Int = 0,
    val derrotas: Int = 0,
    var userName: String? = null,
    var imagen: String = "Imagen",
    var id: String? = null,
    val maxPoints: Int = 0,
    val maxPointsTres: Int = 0,
    val profileImages: List<String> = emptyList(),
    val maxPointsDos: Int = 0,
    val instagram: String? = null,
    val rachaActual: Int = 0,
    val ultimaConexionRacha: com.google.firebase.Timestamp? = null,
    val nationality: String? = null,
    val hasSeenTutorial: Boolean = false,
    val coins: Int = 0,
    val weeklyWins: Int = 0,
    val lastSeasonParticipated: String? = null,
    val lastWeekParticipated: Int? = null
)