package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Pokemon
import com.electrofire.playpkm.Data.Repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repo: PokemonRepository
) : ViewModel() {

    var pokemon by mutableStateOf<Pokemon?>(null)
        private set

    init {
        viewModelScope.launch {
            pokemon = repo.obtenerStatsPokemonDelDia()
        }
    }

}

fun verificarRespuestaStatsPokemon(pokemonActual: Pokemon?, respuesta: String): Boolean {
    return pokemonActual != null && respuesta.trim()
        .equals(pokemonActual.Nombre.trim(), ignoreCase = true)
}