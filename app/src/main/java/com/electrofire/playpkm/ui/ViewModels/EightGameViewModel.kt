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

@HiltViewModel
class EightGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<ImpostorGameData>>(UIState.Loading)
    val state: StateFlow<UIState<ImpostorGameData>> = _state

    init {
        loadGame()
    }

    fun loadGame(){

        viewModelScope.launch{
            _state.value = UIState.Loading

            try {
                val game = repo.obtenerPokemonConMismaHabilidadDelDia()
                _state.value = UIState.Success(game)
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("EIGHT_GAME_VM", "Error: ${e.message}")
                _state.value = UIState.Error("No se pudo cargar el juego.")
            }

        }

    }

}



fun verificarRespuestaEightGame(choisePokemon: String, correctPokemon: String): Boolean {
    return choisePokemon == correctPokemon
}