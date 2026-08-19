package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

data class ElevenGameData(
    val pokemon: PokemonApi,
    val puntaje: Int,
    val isGameOver: Boolean = false
)

@HiltViewModel
class ThousandShadowsViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<ElevenGameData>>(UIState.Loading)
    val state: StateFlow<UIState<ElevenGameData>> = _state

    private val _contador = MutableStateFlow<Int>(18)
    val contador = _contador.asStateFlow()

    private var contadorJob: Job? = null

    init {
        iniciarJuego()
    }

    fun iniciarJuego() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val p = repo.obtenerPokemonRandom()
                if (p != null) {
                    _state.value = UIState.Success(
                        ElevenGameData(
                            pokemon = p,
                            puntaje = 0
                        )
                    )
                    iniciarContador()
                } else {
                    _state.value = UIState.Error("No se pudo cargar el Pokémon.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("SOMBRAS_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

    fun escribirPokemon(pokemonElegido: String) {
        val currentState = _state.value
        if (currentState is UIState.Success && !currentState.data.isGameOver) {
            viewModelScope.launch {
                val pokemonCorrecto = currentState.data.pokemon

                if (pokemonElegido.trim().equals(pokemonCorrecto.name.trim(), ignoreCase = true)) {
                    // Cancelamos el contador actual mientras se carga el siguiente
                    contadorJob?.cancel()
                    val nextP = repo.obtenerPokemonRandom()
                    if (nextP != null) {
                        _state.value = UIState.Success(
                            currentState.data.copy(
                                pokemon = nextP,
                                puntaje = currentState.data.puntaje + 1
                            )
                        )
                        reiniciarContador()
                    } else {
                        _state.value = UIState.Error("Error al cargar el siguiente oponente.")
                    }
                } else {
                    _state.value = UIState.Success(currentState.data.copy(isGameOver = true))
                    contadorJob?.cancel()
                }
            }
        }
    }

    fun iniciarContador() {
        contadorJob?.cancel()
        contadorJob = viewModelScope.launch {
            for (i in 18 downTo 0) {
                _contador.value = i
                if (i == 0) {
                    tiempoAgotado()
                }
                delay(1000L)
            }
        }
    }

    fun reiniciarContador() {
        iniciarContador()
    }

    fun tiempoAgotado() {
        val currentState = _state.value
        if (currentState is UIState.Success) {
            _state.value = UIState.Success(currentState.data.copy(isGameOver = true))
        }
    }

}
