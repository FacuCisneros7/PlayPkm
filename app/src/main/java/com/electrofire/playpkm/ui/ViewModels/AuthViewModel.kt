package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.Repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _userId = MutableLiveData<String?>()

    fun signIn() {
        repository.inicioSesionAnonimo(
            onSucces = { uid -> _userId.value = uid },
            onError = { errorMsg -> Log.e("Auth", errorMsg) }
        )
    }
}
