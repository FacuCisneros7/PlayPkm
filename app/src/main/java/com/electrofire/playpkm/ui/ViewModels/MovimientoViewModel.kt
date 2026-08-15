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

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val repo: MovimientosRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<Movimiento>>(UIState.Loading)
    val state: StateFlow<UIState<Movimiento>> = _state

    init {
        loadMovimiento()
    }

    fun loadMovimiento() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val result = repo.obtenerMovimientoDelDia()
                if (result != null) {
                    _state.value = UIState.Success(result)
                } else {
                    _state.value = UIState.Error("No se encontró el movimiento del día.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("MOVIMIENTO_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
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