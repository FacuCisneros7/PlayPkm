package com.electrofire.playpkm.Data.Repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    //Inicia sesion anonima en firebase
    fun inicioSesionAnonimo(
        //Recibe 2 callbacks (funciones que se ejecutan mas tarde)
        onSucces: (String) -> Unit,
        onError: (String) -> Unit
        ){
        auth.signInAnonymously() //Crea el usuario anonimo
            .addOnSuccessListener {
                val uid = it.user?.uid ?: ""
                onSucces (uid)
            }//Se ejecuta si la autenticacion falla.
            .addOnFailureListener {
                e ->
                onError(e.message ?: "Error desconocido")
            } //Se ejecuta cuando firebase crea el usuario correctamente.
    }
}