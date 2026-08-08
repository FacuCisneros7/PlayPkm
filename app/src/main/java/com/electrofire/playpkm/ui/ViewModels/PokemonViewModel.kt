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

sealed interface FirstGameState {
    data object Loading : FirstGameState
    data class Success(val pokemon: PokemonApi) : FirstGameState
    data class Error(val message: String) : FirstGameState
}

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FirstGameState>(FirstGameState.Loading)
    val state: StateFlow<FirstGameState> = _state

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            _state.value = FirstGameState.Loading
            try {
                val result = repo.obtenerPokemonDelDia()
                if (result != null) {
                    _state.value = FirstGameState.Success(result)
                } else {
                    _state.value = FirstGameState.Error("No se encontró el Pokémon del día.")
                }
            } catch (e: IOException) {
                _state.value = FirstGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("POKEMON_VM", "Error: ${e.message}")
                _state.value = FirstGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaPokemon(pokemonActual: PokemonApi?, respuesta: String): Boolean {
    return pokemonActual != null && respuesta.trim()
        .equals(pokemonActual.name.trim(), ignoreCase = true)
}