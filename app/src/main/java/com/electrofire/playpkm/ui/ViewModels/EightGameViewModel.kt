package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.ImpostorGameData
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EightGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
): ViewModel()
{

    private val _state = MutableStateFlow(ImpostorGameData())
    val state: StateFlow<ImpostorGameData> = _state

    init {
        viewModelScope.launch {

            val game = repo.obtenerPokemonConMismaHabilidadDelDia()

            _state.value = ImpostorGameData(
                abilityName = game.abilityName,
                pokemons = game.pokemons,
                impostor= game.impostor
            )

        }
    }

}

fun verificarRespuestaEightGame(choisePokemon: String, correctPokemon: String): Boolean {
    return choisePokemon == correctPokemon
}