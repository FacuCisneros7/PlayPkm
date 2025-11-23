package com.electrofire.playpkm.Data.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

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

    fun registrarUsuario(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = it.user?.uid ?: ""
                onSuccess(uid)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error desconocido al registrar usuario")
            }
    }

    fun iniciarSesion(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = it.user?.uid ?: ""
                onSuccess(uid)
            }
            .addOnFailureListener { e ->
                val message = when (e) {
                    is FirebaseAuthInvalidUserException -> "El usuario no existe."
                    is FirebaseAuthInvalidCredentialsException -> "Email o contraseña incorrectos."
                    else -> "Error al iniciar sesión: ${e.message}"
                }
                onError(message)
            }
    }

    fun obtenerUsuarioActual(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

}