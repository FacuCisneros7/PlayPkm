package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Carta
import com.electrofire.playpkm.Data.Repository.CartasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartaViewModel @Inject constructor(
    private val repo : CartasRepository
): ViewModel(){


    var carta by mutableStateOf<Carta?>(null)
        private set

    init {
        viewModelScope.launch {
            carta = repo.obtenerCartaDelDia()
        }
    }

}

fun verificarRespuestaCartaBorrosa(cartaActual: Carta?, respuesta: String): Boolean {
    return cartaActual != null && respuesta.trim().equals(cartaActual.Nombre.trim(), ignoreCase = true)
}