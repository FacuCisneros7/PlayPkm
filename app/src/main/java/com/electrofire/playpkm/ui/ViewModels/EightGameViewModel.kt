package com.electrofire.playpkm.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.ImpostorGameData
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface EightGameState {
    data object Loading : EightGameState
    data class Success(val data: ImpostorGameData) : EightGameState
    data class Error(val message: String) : EightGameState
}
@HiltViewModel
class EightGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EightGameState>(EightGameState.Loading)
    val state: StateFlow<EightGameState> = _state

    init {
        loadGame()
    }

    private fun loadGame(){

        viewModelScope.launch{
            _state.value = EightGameState.Loading

            try {
                val game = repo.obtenerPokemonConMismaHabilidadDelDia()
                _state.value = EightGameState.Success(
                    ImpostorGameData(
                        abilityName = game.abilityName,
                        pokemons = game.pokemons,
                        impostor = game.impostor
                    )
                )
            } catch (e: IOException) {
                _state.value = EightGameState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("EIGHT_GAME_VM", "Error: ${e.message}")
                _state.value = EightGameState.Error("No se pudo cargar el juego.")
            }

        }

    }

}



fun verificarRespuestaEightGame(choisePokemon: String, correctPokemon: String): Boolean {
    return choisePokemon == correctPokemon
}