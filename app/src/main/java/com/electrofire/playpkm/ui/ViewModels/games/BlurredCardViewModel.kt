package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Carta
import com.electrofire.playpkm.Data.Repository.LocalGameRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BlurredCardViewModel @Inject constructor(
    private val repo: LocalGameRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<Carta>>(UIState.Loading)
    val state: StateFlow<UIState<Carta>> = _state

    init {
        loadCarta()
    }

    fun loadCarta() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val carta = repo.getCartaDelDia()
                _state.value = UIState.Success(carta)
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("BLURRED_CARD_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }
}

fun verificarRespuestaBlurredCard(cartaActual: Carta?, respuesta: String): Boolean {
    return cartaActual != null && respuesta.trim()
        .equals(cartaActual.Nombre.trim(), ignoreCase = true)
}
