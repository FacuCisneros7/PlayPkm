package com.electrofire.playpkm.Data

data class UserData(
    val victorias: Int = 0,
    val derrotas: Int = 0,
    var userName: String? = null,
    var imagen: String = "Imagen"
)