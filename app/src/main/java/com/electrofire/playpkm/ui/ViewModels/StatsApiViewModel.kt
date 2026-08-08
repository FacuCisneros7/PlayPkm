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

sealed interface FiftGameState {
    data object Loading : FiftGameState
    data class Success(val pokemon: PokemonApi) : FiftGameState
    data class Error(val message: String) : FiftGameState
}

@HiltViewModel
class StatsApiViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FiftGameState>(FiftGameState.Loading)
    val state: StateFlow<FiftGameState> = _state

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            _state.value = FiftGameState.Loading
            try {
                val result = repo.obtenerStatPokemonDelDia()
                if (result != null) {
                    _state.value = FiftGameState.Success(result)
                } else {
                    _state.value = FiftGameState.Error("No se encontró el Pokémon del día.")
                }
            } catch (e: IOException) {
                _state.value = FiftGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("STATS_API_VM", "Error: ${e.message}")
                _state.value = FiftGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaStatsApiPokemon(pokemonActual: PokemonApi?, respuesta: String): Boolean {
    return pokemonActual != null && respuesta.trim()
        .equals(pokemonActual.name.trim(), ignoreCase = true)
}