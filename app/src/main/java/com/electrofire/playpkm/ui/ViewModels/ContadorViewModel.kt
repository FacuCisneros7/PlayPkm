package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContadorViewModel : ViewModel() {

    var contador by mutableStateOf<Int>(60)
        private set

    fun iniciarContador() {
        viewModelScope.launch {
            for (i in 60 downTo 0) {
                contador = i
                delay(1000L)
            }
        }
    }
}