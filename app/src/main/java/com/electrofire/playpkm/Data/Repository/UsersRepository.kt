package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getUsersOrderedByVictories(): List<UserData> {
        return try {
            val snapshot = db.collection("Users")
                .orderBy("victorias", Query.Direction.DESCENDING)
                .orderBy("derrotas", Query.Direction.ASCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUsersOrderedByVictoriesInGC(): List<UserData> {
        return try {
            val snapshot = db.collection("Users")
                .orderBy("maxPoints", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUsersOrderedByVictoriesInTS(): List<UserData> {
        return try {
            val snapshot = db.collection("Users")
                .orderBy("maxPointsDos", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUsersOrderedByVictoriesInBA(): List<UserData> {
        return try {
            val snapshot = db.collection("Users")
                .orderBy("maxPointsTres", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}