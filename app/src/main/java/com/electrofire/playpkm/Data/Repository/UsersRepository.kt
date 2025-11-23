package com.electrofire.playpkm.Data.Repository

import android.util.Log
import com.electrofire.playpkm.Data.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class UsersRepository {
    private val db = FirebaseFirestore.getInstance()

//    fun getUsersOrderedByVictories(onResult: (List<UserData>) -> Unit) {
//        db.collection("Users")
//            .orderBy("victorias", Query.Direction.DESCENDING)
//            .orderBy("derrotas", Query.Direction.ASCENDING)
//            .get()
//            .addOnSuccessListener { snapshot ->
//                val users = snapshot.documents.mapNotNull { doc ->
//                    doc.toObject(UserData::class.java)?.copy(id = doc.id) // 👈 acá guardamos el UID
//                }
//                onResult(users)
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore", "Error al obtener usuarios", e)
//                onResult(emptyList())
//            }
//    }

    fun getUsersOrderedByVictories(onResult: (List<UserData>) -> Unit): ListenerRegistration {
        return db.collection("Users")
            .orderBy("victorias", Query.Direction.DESCENDING)
            .orderBy("derrotas", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error al escuchar usuarios", error)
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(UserData::class.java)?.copy(id = doc.id)
                    }
                    onResult(users)
                }
            }
    }

}