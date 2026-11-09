package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Fusion
import com.electrofire.playpkm.Data.Repository.LocalGameRepository
import com.electrofire.playpkm.Domain.isAnswerCorrect
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FusionViewModel @Inject constructor(
    private val repo: LocalGameRepository
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
                val fusion = repo.getFusionDelDia()
                _state.value = UIState.Success(fusion)
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
    if (fusionActual != null && fusionActual.Pokemones.size >= 2) {
        val pkm1 = fusionActual.Pokemones[0]
        val pkm2 = fusionActual.Pokemones[1]
        val match1 = (isAnswerCorrect(pkm1, respuesta) && isAnswerCorrect(pkm2, respuestaDos))
        val match2 = (isAnswerCorrect(pkm1, respuestaDos) && isAnswerCorrect(pkm2, respuesta))
        return match1 || match2
    }
    return false
}
