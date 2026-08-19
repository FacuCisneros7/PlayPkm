package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class AbilityViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow<UIState<PokemonApi>>(UIState.Loading)
    val state: StateFlow<UIState<PokemonApi>> = _state

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val result = repo.obtenerHabilidadPokemonDelDia()
                if (result != null) {
                    _state.value = UIState.Success(result)
                } else {
                    _state.value = UIState.Error("No se encontró el Pokémon del día.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("HABILITY_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

}

fun verificarRespuestaAbility(pokemonActual: PokemonApi?, respuesta: String): Boolean {
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
