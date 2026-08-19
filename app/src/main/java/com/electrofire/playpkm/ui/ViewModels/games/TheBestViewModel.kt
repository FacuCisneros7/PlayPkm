package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.GameState
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TheBestViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<GameState>>(UIState.Loading)
    val state: StateFlow<UIState<GameState>> = _state

    init {
        loadGame()
    }

    fun loadGame() {
        viewModelScope.launch {
            _state.value = UIState.Loading
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

                    _state.value = UIState.Success(
                        GameState(
                            pokemons = pokemons,
                            selectedStat = spanishStat,
                            selectedStatEnglish = englishStat,
                            correctPokemons = correctPokemons
                        )
                    )
                } else {
                    _state.value = UIState.Error("No se pudieron cargar los Pokémon.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("SEVENTH_GAME_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaTheBest(
    choisePokemon: PokemonApi,
    correctPokemons: List<PokemonApi>
): Boolean {
    return correctPokemons.contains(choisePokemon)
}
