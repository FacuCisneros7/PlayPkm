package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.Normalizer
import javax.inject.Inject

sealed interface ThirdGameState {
    data object Loading : ThirdGameState
    data class Success(val pokemon: PokemonApi) : ThirdGameState
    data class Error(val message: String) : ThirdGameState
}

@HiltViewModel
class HabilityViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow<ThirdGameState>(ThirdGameState.Loading)
    val state: StateFlow<ThirdGameState> = _state

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            _state.value = ThirdGameState.Loading
            try {
                val result = repo.obtenerHabilidadPokemonDelDia()
                if (result != null) {
                    _state.value = ThirdGameState.Success(result)
                } else {
                    _state.value = ThirdGameState.Error("No se encontró el Pokémon del día.")
                }
            } catch (e: IOException) {
                _state.value = ThirdGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("HABILITY_VM", "Error: ${e.message}")
                _state.value = ThirdGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaHabilidadPokemon(pokemonActual: PokemonApi?, respuesta: String): Boolean {
    fun String.normalizar(): String =
        Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "") // elimina los acentos
            .trim()
            .lowercase()

    if (pokemonActual != null) {
        val respuestaNormalizada = respuesta.normalizar()
        return pokemonActual.abilities.any { habilidad ->
            habilidad.normalizar() == respuestaNormalizada
        }
    }
    return false
}