package com.electrofire.playpkm.Data

import com.google.firebase.firestore.PropertyName

data class Movimiento(
    @get:PropertyName("p") @set:PropertyName("p") var p: Int = 0, // Potencia
    @get:PropertyName("i") @set:PropertyName("i") var i: String = "" // Imagen/ID
)
