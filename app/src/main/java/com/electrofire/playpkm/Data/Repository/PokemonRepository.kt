package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.LocalData.PokemonDao
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val dao: PokemonDao
) {
    private val timeRepository = TimeRepository()
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Pokemon")


}