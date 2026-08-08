package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Carta
import com.electrofire.playpkm.Data.Repository.CartasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface SecondGameState {
    data object Loading : SecondGameState
    data class Success(val carta: Carta) : SecondGameState
    data class Error(val message: String) : SecondGameState
}

@HiltViewModel
class CartaViewModel @Inject constructor(
    private val repo: CartasRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SecondGameState>(SecondGameState.Loading)
    val state: StateFlow<SecondGameState> = _state

    init {
        loadCarta()
    }

    fun loadCarta() {
        viewModelScope.launch {
            _state.value = SecondGameState.Loading
            try {
                val result = repo.obtenerCartaDelDia()
                if (result != null) {
                    _state.value = SecondGameState.Success(result)
                } else {
                    _state.value = SecondGameState.Error("No se encontró la carta del día.")
                }
            } catch (e: IOException) {
                _state.value = SecondGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("CARTA_VM", "Error: ${e.message}")
                _state.value = SecondGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaCartaBorrosa(cartaActual: Carta?, respuesta: String): Boolean {
    return cartaActual != null && respuesta.trim()
        .equals(cartaActual.Nombre.trim(), ignoreCase = true)
}