package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Fusion
import com.electrofire.playpkm.Data.Repository.FusionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FusionViewModel @Inject constructor(
    private val repo: FusionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<Fusion>>(UIState.Loading)
    val state: StateFlow<UIState<Fusion>> = _state

    init {
        loadFusion()
    }

    fun loadFusion() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val result = repo.obtenerFusionDelDia()
                if (result != null) {
                    _state.value = UIState.Success(result)
                } else {
                    _state.value = UIState.Error("No se encontró la fusión del día.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("FUSION_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaFusion(
    fusionActual: Fusion?,
    respuesta: String,
    respuestaDos: String
): Boolean {
    if (fusionActual != null) {
        return fusionActual.Pokemones.any { it.equals(respuesta.trim(), ignoreCase = true) } &&
                fusionActual.Pokemones.any { it.equals(respuestaDos.trim(), ignoreCase = true) }
    }
    return false
}