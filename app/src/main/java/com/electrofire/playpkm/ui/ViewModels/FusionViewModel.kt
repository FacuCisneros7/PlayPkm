package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Fusion
import com.electrofire.playpkm.Data.Repository.FusionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FusionViewModel @Inject constructor(
    private val repo: FusionRepository
) : ViewModel() {

    var fusion by mutableStateOf<Fusion?>(null)
        private set

    init {
        viewModelScope.launch {
            fusion = repo.obtenerFusionDelDia()
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