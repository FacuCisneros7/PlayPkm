package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.math.abs

data class WeightGameData(
    val pokemonA: PokemonApi,
    val pokemonB: PokemonApi,
    val currentStep: Int = 1,
    val selectedPokemon: PokemonApi? = null,
    val isAnswerRevealed: Boolean = false,
    val isLoadingNextStep: Boolean = false,
    val isGameOver: Boolean = false,
    val isWin: Boolean = false,
    val finalPokemonA: PokemonApi? = null,
    val finalPokemonB: PokemonApi? = null
)

@HiltViewModel
class WeightGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<WeightGameData>>(UIState.Loading)
    val state: StateFlow<UIState<WeightGameData>> = _state.asStateFlow()

    init {
        iniciarJuego()
    }

    fun iniciarJuego() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val pair = obtenerParParaStep(1)
                if (pair != null) {
                    _state.value = UIState.Success(
                        WeightGameData(
                            pokemonA = pair.first,
                            pokemonB = pair.second,
                            currentStep = 1,
                            selectedPokemon = null,
                            isAnswerRevealed = false,
                            isLoadingNextStep = false,
                            isGameOver = false,
                            isWin = false,
                            finalPokemonA = null,
                            finalPokemonB = null
                        )
                    )
                } else {
                    _state.value = UIState.Error("No se pudieron cargar los Pokémon.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("WEIGHT_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

    private suspend fun obtenerParParaStep(step: Int): Pair<PokemonApi, PokemonApi>? {
        val maxAttempts = 50
        var bestPair: Pair<PokemonApi, PokemonApi>? = null
        var bestDiff = if (step == 5) Double.MAX_VALUE else -1.0

        for (i in 0 until maxAttempts) {
            val a = repo.obtenerPokemonRandom()
            val b = repo.obtenerPokemonRandom()
            if (a != null && b != null && a.name != b.name) {
                val diff = abs(a.weight - b.weight).toDouble() // En hectogramos (hg)

                val cumple = when (step) {
                    1 -> diff > 600.0               // > 60 kg
                    2 -> diff in 300.0..600.0       // 30 kg a 60 kg
                    3 -> diff in 150.0..300.0       // 15 kg a 30 kg
                    4 -> diff in 80.0..150.0        // 8 kg a 15 kg
                    5 -> diff < 80.0                // < 8 kg
                    else -> true
                }

                if (cumple) {
                    return a to b
                }

                if (step == 5) {
                    if (diff < bestDiff) {
                        bestDiff = diff
                        bestPair = a to b
                    }
                } else {
                    val targetMid = when (step) {
                        1 -> 800.0
                        2 -> 450.0
                        3 -> 225.0
                        4 -> 115.0
                        else -> 0.0
                    }
                    val currentDistance = abs(diff - targetMid)
                    val bestDistance = if (bestPair != null) abs(abs(bestPair.first.weight - bestPair.second.weight) - targetMid) else Double.MAX_VALUE
                    if (currentDistance < bestDistance) {
                        bestDiff = diff
                        bestPair = a to b
                    }
                }
            }
        }

        return bestPair ?: run {
            val fallbackA = repo.obtenerPokemonRandom()
            val fallbackB = repo.obtenerPokemonRandom()
            if (fallbackA != null && fallbackB != null) fallbackA to fallbackB else null
        }
    }

    fun seleccionarPokemon(pokemon: PokemonApi) {
        val currentState = _state.value
        if (currentState is UIState.Success && !currentState.data.isGameOver) {
            val data = currentState.data
            val a = data.pokemonA
            val b = data.pokemonB

            val weightA = a.weight
            val weightB = b.weight

            val esEmpate = weightA == weightB
            val pokemonCorrecto = when {
                esEmpate -> pokemon
                weightA > weightB -> a
                else -> b
            }

            val respuestaCorrecta = if (esEmpate) true else pokemon.name == pokemonCorrecto.name

            if (!respuestaCorrecta) {
                // Falló inmediatamente: Game Over sin pasar por continuar
                _state.value = UIState.Success(
                    data.copy(
                        selectedPokemon = pokemon,
                        isGameOver = true,
                        isWin = false,
                        finalPokemonA = a,
                        finalPokemonB = b
                    )
                )
            } else {
                // Acertó: selecciona y revela los pesos, esperando el botón Continuar
                _state.value = UIState.Success(
                    data.copy(
                        selectedPokemon = pokemon,
                        isAnswerRevealed = true
                    )
                )
            }
        }
    }

    fun continuarSiguientePaso() {
        val currentState = _state.value
        if (currentState is UIState.Success && !currentState.data.isLoadingNextStep) {
            viewModelScope.launch {
                _state.value = UIState.Success(currentState.data.copy(isLoadingNextStep = true))

                val data = currentState.data
                val nextStep = data.currentStep + 1
                if (nextStep > 5) {
                    _state.value = UIState.Success(
                        data.copy(
                            isGameOver = true,
                            isWin = true,
                            isLoadingNextStep = false,
                            finalPokemonA = data.pokemonA,
                            finalPokemonB = data.pokemonB
                        )
                    )
                } else {
                    val nextPair = obtenerParParaStep(nextStep)
                    if (nextPair != null) {
                        _state.value = UIState.Success(
                            WeightGameData(
                                pokemonA = nextPair.first,
                                pokemonB = nextPair.second,
                                currentStep = nextStep,
                                selectedPokemon = null,
                                isAnswerRevealed = false,
                                isLoadingNextStep = false,
                                isGameOver = false,
                                isWin = false,
                                finalPokemonA = null,
                                finalPokemonB = null
                            )
                        )
                    } else {
                        _state.value = UIState.Error("Error al cargar el siguiente enfrentamiento.")
                    }
                }
            }
        }
    }
}
