package com.electrofire.playpkm.Data

data class Pokemon(
    val Nombre: String = "",
    val Descripcion: String = "",
    val Imagen: String = "",
    val Habilidades: ArrayList<String> = arrayListOf(),
    val Stats: String = ""
)