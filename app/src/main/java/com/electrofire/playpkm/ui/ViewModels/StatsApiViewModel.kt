package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsApiViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    var pokemon by mutableStateOf<PokemonApi?>(null)
        private set

    init {
        viewModelScope.launch {
            pokemon = repo.obtenerStatPokemonDelDia()
        }
    }

}

fun verificarRespuestaStatsApiPokemon(pokemonActual: PokemonApi?, respuesta: String): Boolean {
    return pokemonActual != null && respuesta.trim()
        .equals(pokemonActual.name.trim(), ignoreCase = true)
}