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

sealed interface NinthGameState {
    data object Loading : NinthGameState
    data class Success(
        val pokemonA: PokemonApi,
        val pokemonB: PokemonApi,
        val puntaje: Int,
        val isGameOver: Boolean = false
    ) : NinthGameState
    data class Error(val message: String) : NinthGameState
}

@HiltViewModel
class NinthViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NinthGameState>(NinthGameState.Loading)
    val state: StateFlow<NinthGameState> = _state

    init {
        iniciarJuego()
    }

    fun iniciarJuego() {
        viewModelScope.launch {
            _state.value = NinthGameState.Loading
            try {
                val a = repo.obtenerPokemonRandom()
                val b = repo.obtenerPokemonRandom()
                if (a != null && b != null) {
                    _state.value = NinthGameState.Success(
                        pokemonA = a,
                        pokemonB = b,
                        puntaje = 0
                    )
                } else {
                    _state.value = NinthGameState.Error("No se pudieron cargar los Pokémon.")
                }
            } catch (e: IOException) {
                _state.value = NinthGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("NINTH_VM", "Error: ${e.message}")
                _state.value = NinthGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

    fun elegirPokemon(pokemonElegido: PokemonApi) {
        val currentState = _state.value
        if (currentState is NinthGameState.Success) {
            viewModelScope.launch {
                val a = currentState.pokemonA
                val b = currentState.pokemonB

                val bstA = a.stats.values.sum()
                val bstB = b.stats.values.sum()

                val esEmpate = bstA == bstB
                val pokemonCorrecto = if (bstA > bstB) a else b

                val respuestaCorrecta =
                    if (esEmpate) true
                    else pokemonElegido.name == pokemonCorrecto.name

                if (respuestaCorrecta) {
                    val nextB = repo.obtenerPokemonRandom()
                    if (nextB != null) {
                        _state.value = currentState.copy(
                            pokemonA = b,
                            pokemonB = nextB,
                            puntaje = currentState.puntaje + 1
                        )
                    } else {
                        _state.value = NinthGameState.Error("Error al cargar el siguiente oponente.")
                    }
                } else {
                    _state.value = currentState.copy(isGameOver = true)
                }
            }
        }
    }

}