package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.GameState
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface SeventhGameState {
    data object Loading : SeventhGameState
    data class Success(val data: GameState) : SeventhGameState
    data class Error(val message: String) : SeventhGameState
}

@HiltViewModel
class SeventhGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SeventhGameState>(SeventhGameState.Loading)
    val state: StateFlow<SeventhGameState> = _state

    init {
        loadGame()
    }

    fun loadGame() {
        viewModelScope.launch {
            _state.value = SeventhGameState.Loading
            try {
                val pokemons = repo.getRandomPokemons()
                if (pokemons.isNotEmpty()) {
                    val englishStat =
                        listOf("special-attack", "attack", "hp", "defense", "speed", "special-defense")
                            .random()
                    var spanishStat = englishStat

                    val maxStatValue = pokemons.maxOfOrNull {
                        it.stats[englishStat] ?: 0
                    } ?: 0

                    val correctPokemons = pokemons.filter {
                        it.stats[englishStat] == maxStatValue
                    }

                    when (spanishStat) {
                        "special-attack" -> spanishStat = "Ataque Especial"
                        "special-defense" -> spanishStat = "Defensa Especial"
                        "attack" -> spanishStat = "Ataque Físico"
                        "defense" -> spanishStat = "Defensa Física"
                        "speed" -> spanishStat = "Velocidad"
                        "hp" -> spanishStat = "PS"
                    }

                    _state.value = SeventhGameState.Success(
                        GameState(
                            pokemons = pokemons,
                            selectedStat = spanishStat,
                            selectedStatEnglish = englishStat,
                            correctPokemons = correctPokemons
                        )
                    )
                } else {
                    _state.value = SeventhGameState.Error("No se pudieron cargar los Pokémon.")
                }
            } catch (e: IOException) {
                _state.value = SeventhGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("SEVENTH_GAME_VM", "Error: ${e.message}")
                _state.value = SeventhGameState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaSeventhGame(
    choisePokemon: PokemonApi,
    correctPokemons: List<PokemonApi>
): Boolean {
    return correctPokemons.contains(choisePokemon)
}