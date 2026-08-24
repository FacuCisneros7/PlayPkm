package com.electrofire.playpkm.Data

import com.google.firebase.firestore.PropertyName

data class Carta(
    @get:PropertyName("Nombre") @set:PropertyName("Nombre") var Nombre: String = "",
    @get:PropertyName("ImagenBorrosa") @set:PropertyName("ImagenBorrosa") var ImagenBorrosa: String = "",
    @get:PropertyName("ImagenReal") @set:PropertyName("ImagenReal") var ImagenReal: String = ""
)
