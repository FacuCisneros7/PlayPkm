package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Movimiento
import com.electrofire.playpkm.Data.Repository.MovimientosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val repo: MovimientosRepository
) : ViewModel() {

    var movimiento by mutableStateOf<Movimiento?>(null)
        private set

    init {
        viewModelScope.launch {
            movimiento = repo.obtenerMovimientoDelDia()
        }
    }

}

fun verificarRespuestaPotenciaMovimiento(
    respuesta: String,
    movimientoActual: Movimiento?
): Boolean {
    if (respuesta != "") {
        return movimientoActual != null && respuesta.toInt() == movimientoActual.p
    }
    return false
}