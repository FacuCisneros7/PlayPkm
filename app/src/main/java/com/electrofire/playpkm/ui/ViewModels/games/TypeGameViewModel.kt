package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.Data.PokemonTypeUtils
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TypeGameViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<PokemonApi>>(UIState.Loading)
    val state: StateFlow<UIState<PokemonApi>> = _state

    init {
        loadRandomPokemon()
    }

    fun loadRandomPokemon() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val pokemon = repo.obtenerPokemonRandom()
                if (pokemon != null) {
                    _state.value = UIState.Success(pokemon)
                } else {
                    _state.value = UIState.Error("No se pudo obtener un Pokémon.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("TYPE_GAME_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado.")
            }
        }
    }

    fun checkAnswer(pokemon: PokemonApi, type1: String, type2: String): Boolean {
        // Obtenemos los tipos reales del pokemon. 
        // repo.obtenerPokemonRandom() mapea types = types.associate { it.type.name to it.slot }
        // Slot 1 es el tipo primario, Slot 2 el secundario (si existe)
        
        val actualType1 = pokemon.types.entries.find { it.value == 1 }?.key ?: ""
        val actualType2 = pokemon.types.entries.find { it.value == 2 }?.key ?: "none"

        // Convertimos la selección del usuario (que viene en español) a inglés para comparar
        val selectedType1Eng = PokemonTypeUtils.types.find { it.spanishName == type1 }?.englishName ?: ""
        val selectedType2Eng = PokemonTypeUtils.typesWithEmpty.find { it.spanishName == type2 }?.englishName ?: "none"

        return actualType1 == selectedType1Eng && actualType2 == selectedType2Eng
    }
}
