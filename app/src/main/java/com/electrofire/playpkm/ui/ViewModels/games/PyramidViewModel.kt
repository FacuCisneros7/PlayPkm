package com.electrofire.playpkm.ui.ViewModels.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PyramidViewModel @Inject constructor(
    private val repository: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<List<PokemonApi>>>(UIState.Loading)
    val state: StateFlow<UIState<List<PokemonApi>>> = _state.asStateFlow()

    private val _selectedIndices = MutableStateFlow<Pair<Int?, Int?>>(null to null)
    val selectedIndices = _selectedIndices.asStateFlow()

    init {
        loadGame()
    }

    fun loadGame() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val pokemons = repository.getRandomPokemons(6)
                if (pokemons.size == 6) {
                    _state.value = UIState.Success(pokemons.shuffled())
                } else {
                    _state.value = UIState.Error("Error al cargar los Pokémon")
                }
            } catch (e: Exception) {
                _state.value = UIState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun onCardSelected(index: Int) {
        val current = _selectedIndices.value
        if (current.first == null) {
            _selectedIndices.value = index to null
        } else if (current.first == index) {
            _selectedIndices.value = null to null
        } else {
            // Realizar el cambio
            swapCards(current.first!!, index)
            _selectedIndices.value = null to null
        }
    }

    fun swapCards(fromIndex: Int, toIndex: Int) {
        val currentState = _state.value
        if (currentState is UIState.Success) {
            val list = currentState.data.toMutableList()
            val temp =
                list[fromIndex] //Creamos temp para que no se pierda el pokemon 1 antes de reemplazarlo por el 2.
            list[fromIndex] =
                list[toIndex] //No se reemplaza la lista, se reemplaza solo el pokemon 1 por el 2.
            list[toIndex] = temp //Usamos el temp del pokemon 1 para reemplazar al pokemon 2.
            _state.value = UIState.Success(list)
        }
    }

    fun verifyOrder(currentList: List<PokemonApi>): Boolean {
        // Calcular BST para cada pokemon
        val bstList = currentList.map { it.stats.values.sum() }

        for (i in 0 until bstList.size - 1) {
            if (bstList[i] < bstList[i + 1]) return false
        }
        return true
    }
}
