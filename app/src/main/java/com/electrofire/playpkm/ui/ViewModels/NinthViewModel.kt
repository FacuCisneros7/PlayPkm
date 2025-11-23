package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NinthViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _pokemonA = MutableStateFlow<PokemonApi?>(null)
    val pokemonA = _pokemonA.asStateFlow()

    private val _pokemonB = MutableStateFlow<PokemonApi?>(null)
    val pokemonB = _pokemonB.asStateFlow()

    private val _puntaje = MutableStateFlow(0)
    val puntaje = _puntaje.asStateFlow()

    private val _estadoJuego = MutableStateFlow("playing")
    val estadoJuego = _estadoJuego.asStateFlow()

    init {
        viewModelScope.launch {
            iniciarJuego()
        }
    }


    fun iniciarJuego() {
        viewModelScope.launch {
            _puntaje.value = 0
            _estadoJuego.value = "playing"
            _pokemonA.value = repo.obtenerPokemonRandom()
            _pokemonB.value = repo.obtenerPokemonRandom()
        }
    }

    fun elegirPokemon(pokemonElegido: PokemonApi) {
        viewModelScope.launch {
            val a = _pokemonA.value ?: return@launch
            val b = _pokemonB.value ?: return@launch

            val bstA = a.stats.values.sum()
            val bstB = b.stats.values.sum()

            val esEmpate = bstA == bstB

            val pokemonCorrecto = if (bstA > bstB) a else b

            val respuestaCorrecta =
                if (esEmpate) true  // cualquier elección vale
                else pokemonElegido.name == pokemonCorrecto.name

            if (respuestaCorrecta) {
                _puntaje.value += 1
                _pokemonA.value = _pokemonB.value
                _pokemonB.value = repo.obtenerPokemonRandom() // se genera otro rival
            } else {
                _estadoJuego.value = "game_over"
            }
        }
    }


}