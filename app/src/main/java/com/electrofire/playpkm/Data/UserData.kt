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
)