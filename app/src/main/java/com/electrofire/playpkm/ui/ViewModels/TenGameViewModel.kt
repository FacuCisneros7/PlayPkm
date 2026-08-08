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
import javax.inject.Inject

sealed interface TenGameState {
    data object Loading : TenGameState
    data class Success(val pokemon: PokemonApi) : TenGameState
    data class Error(val message: String) : TenGameState
}

@HiltViewModel
class TenGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TenGameState>(TenGameState.Loading)
    val state: StateFlow<TenGameState> = _state

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            _state.value = TenGameState.Loading
            try {
                val result = repo.obtenerPokemonDelDiaConZoom()
                if (result != null) {
                    _state.value = TenGameState.Success(result)
                } else {
                    _state.value = TenGameState.Error("No se encontró el Pokémon del día.")
                }
            } catch (e: IOException) {
                _state.value = TenGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("TEN_GAME_VM", "Error: ${e.message}")
                _state.value = TenGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaPokemonConZoom(pokemonActual: PokemonApi?, respuesta: String): Boolean {
    return pokemonActual != null && respuesta.trim()
        .equals(pokemonActual.name.trim(), ignoreCase = true)
}