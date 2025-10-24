package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Pokemon
import com.electrofire.playpkm.Data.Repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DosSiluetasPokemonViewModel @Inject constructor(
    private val repo: PokemonRepository
): ViewModel() {
    var pokemonUno by mutableStateOf<Pokemon?>(null)
        private set
    var pokemonDos by mutableStateOf<Pokemon?>(null)
        private set


    init {
        viewModelScope.launch {
            val unoDeferred = async { repo.obtenerPokemonSiluetaUnoDia() }
            val dosDeferred = async { repo.obtenerPokemonSiluetaDosDia() }

            // Espera a que ambos terminen
            val uno = unoDeferred.await()
            val dos = dosDeferred.await()

            // Asigna los dos juntos
            pokemonUno = uno
            pokemonDos = dos
        }
    }
}

fun verificarRespuestaDosSiluetas(pokemonActualUno: Pokemon?, pokemonActualDos: Pokemon?, respuesta: String, respuestaDos: String): Boolean {
    if(pokemonActualUno != null && pokemonActualDos != null){
        return respuesta.trim().equals(pokemonActualDos.Nombre.trim(), ignoreCase = true)
                && respuestaDos.trim().equals(pokemonActualUno.Nombre.trim(), ignoreCase = true)
                || respuesta.trim().equals(pokemonActualUno.Nombre.trim(), ignoreCase = true)
                && respuestaDos.trim().equals(pokemonActualDos.Nombre.trim(), ignoreCase = true)
    }
    return false
}