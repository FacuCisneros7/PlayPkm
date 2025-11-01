package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Pokemon
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.Data.Repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repo: PokemonApiRepository
    ): ViewModel(){

    var pokemon by mutableStateOf<PokemonApi?>(null)
    private set

    init {
        viewModelScope.launch {
            pokemon = repo.obtenerPokemonDelDia()
        }
    }

}

fun verificarRespuestaPokemon(pokemonActual: PokemonApi?, respuesta: String): Boolean {
    return pokemonActual != null && respuesta.trim().equals(pokemonActual.name.trim(), ignoreCase = true)
}