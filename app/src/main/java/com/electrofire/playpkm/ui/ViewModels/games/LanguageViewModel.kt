package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.LanguageGameData
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<LanguageGameData>>(UIState.Loading)
    val state: StateFlow<UIState<LanguageGameData>> = _state

    init {
        loadGame()
    }

    fun loadGame() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val result = repo.obtenerPokemonPorIdiomasDelDia()
                if (result != null) {
                    _state.value = UIState.Success(result)
                } else {
                    _state.value = UIState.Error("No se pudo cargar el juego de idiomas.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("LANGUAGE_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }
}

fun verificarRespuestaLanguage(gameData: LanguageGameData?, respuesta: String): Boolean {
    fun String.normalizar(): String =
        Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
            .trim()
            .lowercase()

    if (gameData != null) {
        return respuesta.normalizar() == gameData.pokemon.name.normalizar()
    }
    return false
}
