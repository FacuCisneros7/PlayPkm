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
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class HabilityViewModel @Inject constructor(
    private val repo: PokemonApiRepository
): ViewModel(){
    var pokemon by mutableStateOf<PokemonApi?>(null)
        private set

    init {
        viewModelScope.launch {
            pokemon = repo.obtenerHabilidadPokemonDelDia()
        }
    }

}
fun verificarRespuestaHabilidadPokemon(pokemonActual: PokemonApi?, respuesta: String): Boolean {
    fun String.normalizar(): String =
        Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "") // elimina los acentos
            .trim()
            .lowercase()

    if (pokemonActual != null) {
        val respuestaNormalizada = respuesta.normalizar()
        return pokemonActual.abilities.any { habilidad ->
            habilidad.normalizar() == respuestaNormalizada
        }
    }
    return false
}