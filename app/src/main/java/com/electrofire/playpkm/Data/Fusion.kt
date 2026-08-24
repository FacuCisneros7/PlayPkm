package com.electrofire.playpkm.Data

import com.google.firebase.firestore.PropertyName

data class Fusion(
    @get:PropertyName("id") @set:PropertyName("id") var id: Int = 0,
    @get:PropertyName("Imagen") @set:PropertyName("Imagen") var Imagen: String = "",
    @get:PropertyName("Pokemones") @set:PropertyName("Pokemones") var Pokemones: ArrayList<String> = arrayListOf()
)
