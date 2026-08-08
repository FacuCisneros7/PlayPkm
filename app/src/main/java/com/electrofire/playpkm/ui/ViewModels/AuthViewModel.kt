package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.Repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _userId = MutableLiveData<String?>()

    fun register(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        repository.registrarUsuario(
            email = email,
            password = password,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        repository.iniciarSesion(
            email = email,
            password = password,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun signIn() {
        repository.inicioSesionAnonimo(
            onSucces = { uid -> _userId.value = uid },
            onError = { errorMsg -> Log.e("Auth", errorMsg) }
        )
    }

    fun checkUserLoggedIn() {
        _userId.value = repository.obtenerUsuarioActual()
    }

    fun resetPassword(email: String, onResult: (Boolean, String) -> Unit) {
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Te enviamos un email para restablecer tu contraseña")
                } else {
                    onResult(false, task.exception?.localizedMessage ?: "Error")
                }
            }
    }

}
