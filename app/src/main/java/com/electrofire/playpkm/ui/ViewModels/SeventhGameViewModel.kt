package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.GameState
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeventhGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
): ViewModel()
{

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    init {
        viewModelScope.launch {

            val pokemons = repo.getRandomPokemons()
            val englishStat = listOf("special-attack","attack","hp","defense","speed","special-defense")
                .random()
            var spanishStat = englishStat
            val correct = pokemons.maxByOrNull { it.stats[englishStat] ?:0 } //Obtenemos al pokemon correcto basandonos en la stat elegida.

            when(spanishStat){
                "special-attack"-> spanishStat = "Ataque Especial"
                "special-defense"-> spanishStat = "Defensa Especial"
                "attack"-> spanishStat = "Ataque Físico"
                "defense"-> spanishStat = "Defensa Física"
                "speed"-> spanishStat = "Velocidad"
                "hp"-> spanishStat = "PS"
            }

            _state.value = GameState(
                pokemons = pokemons,
                selectedStat = spanishStat,
                selectedStatEnglish = englishStat,
                correctPokemon = correct
            )

        }
    }

}

fun verificarRespuestaSeventhGame(choisePokemon: PokemonApi, correctPokemon: PokemonApi): Boolean {
    return choisePokemon == correctPokemon
}