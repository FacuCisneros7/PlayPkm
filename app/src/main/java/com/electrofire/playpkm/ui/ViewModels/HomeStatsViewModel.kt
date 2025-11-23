package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeStatsViewModel : ViewModel() {

    var userData by mutableStateOf(UserData())
    private set

    var isUserLoaded by mutableStateOf(false)
        private set

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val userId: String?
        get() = auth.currentUser?.uid

    init {
        cargarStats()
    }

    fun registrarVictoria(){
        userData = userData.copy(victorias = userData.victorias + 1)
        guardarStats()
    }
    fun registrarDerrota(){
        userData = userData.copy(derrotas = userData.derrotas + 1)
        guardarStats()
    }

   fun registrarUserName(userName : String){
        userData = userData.copy(userName = userName)
        guardarStats()
    }

    fun registrarFoto(image : String){
        userData = userData.copy(imagen = image)
        guardarStats()
    }

    private fun guardarStats(){
        userId?.let { uid -> //Si userId no es nulo, ejecuta el bloque y asigna su valor a uid
            firestore.collection("Users")
                .document(uid)
                .set(userData) //Guarda los datos actuales de data stats en el documento.
                .addOnSuccessListener { Log.d("Firestore","Stats guardados") }
                .addOnFailureListener { Log.e("Firestore","Error: ${it.message}") }
        }
    }

    fun cargarStats() {
        userId?.let { uid ->
            firestore.collection("Users").document(uid).get()
                .addOnSuccessListener { document ->
                    userData = document.toObject(UserData::class.java) ?: UserData()
                    isUserLoaded = true
                }
                .addOnFailureListener {
                    userData = UserData()
                    isUserLoaded = true
                }
        } ?: run {
            userData = UserData()
            isUserLoaded = true
        }
    }

    fun registrarMaxScoreNinthGame(score: Int) {
        if (score > userData.maxPoints) {
            userData = userData.copy(maxPoints = score)
            guardarStats()
        }
    }

    fun reset() {
        userData = userData.copy(
            victorias = 0,
            derrotas = 0,
            maxPoints = 0
        )
    }

}