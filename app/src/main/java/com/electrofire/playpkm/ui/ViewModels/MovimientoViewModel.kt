package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Movimiento
import com.electrofire.playpkm.Data.Repository.MovimientosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface FourthGameState {
    data object Loading : FourthGameState
    data class Success(val movimiento: Movimiento) : FourthGameState
    data class Error(val message: String) : FourthGameState
}

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val repo: MovimientosRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FourthGameState>(FourthGameState.Loading)
    val state: StateFlow<FourthGameState> = _state

    init {
        loadMovimiento()
    }

    fun loadMovimiento() {
        viewModelScope.launch {
            _state.value = FourthGameState.Loading
            try {
                val result = repo.obtenerMovimientoDelDia()
                if (result != null) {
                    _state.value = FourthGameState.Success(result)
                } else {
                    _state.value = FourthGameState.Error("No se encontró el movimiento del día.")
                }
            } catch (e: IOException) {
                _state.value = FourthGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("MOVIMIENTO_VM", "Error: ${e.message}")
                _state.value = FourthGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaPotenciaMovimiento(
    respuesta: String,
    movimientoActual: Movimiento?
): Boolean {
    if (respuesta != "") {
        return try {
            movimientoActual != null && respuesta.toInt() == movimientoActual.p
        } catch (e: NumberFormatException) {
            false
        }
    }
    return false
}