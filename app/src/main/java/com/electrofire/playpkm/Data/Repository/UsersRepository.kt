package com.electrofire.playpkm.Data.Repository

import android.util.Log
import com.electrofire.playpkm.Data.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UsersRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getUsersOrderedByVictories(onResult: (List<UserData>) -> Unit) {
        db.collection("Users")
            .orderBy("victorias", Query.Direction.DESCENDING)
            .orderBy("derrotas", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.mapNotNull { it.toObject(UserData::class.java) }
                onResult(users)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener usuarios", e)
                onResult(emptyList())
            }
    }
}