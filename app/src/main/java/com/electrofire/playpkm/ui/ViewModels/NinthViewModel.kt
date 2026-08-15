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

data class NinthGameData(
    val pokemonA: PokemonApi,
    val pokemonB: PokemonApi,
    val puntaje: Int,
    val isGameOver: Boolean = false
)

@HiltViewModel
class NinthViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<NinthGameData>>(UIState.Loading)
    val state: StateFlow<UIState<NinthGameData>> = _state

    init {
        iniciarJuego()
    }

    fun iniciarJuego() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val a = repo.obtenerPokemonRandom()
                val b = repo.obtenerPokemonRandom()
                if (a != null && b != null) {
                    _state.value = UIState.Success(
                        NinthGameData(
                            pokemonA = a,
                            pokemonB = b,
                            puntaje = 0
                        )
                    )
                } else {
                    _state.value = UIState.Error("No se pudieron cargar los Pokémon.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("NINTH_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

    fun elegirPokemon(pokemonElegido: PokemonApi) {
        val currentState = _state.value
        if (currentState is UIState.Success) {
            viewModelScope.launch {
                val a = currentState.data.pokemonA
                val b = currentState.data.pokemonB

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
                        _state.value = UIState.Success(
                            currentState.data.copy(
                                pokemonA = b,
                                pokemonB = nextB,
                                puntaje = currentState.data.puntaje + 1
                            )
                        )
                    } else {
                        _state.value = UIState.Error("Error al cargar el siguiente oponente.")
                    }
                } else {
                    _state.value = UIState.Success(currentState.data.copy(isGameOver = true))
                }
            }
        }
    }

}