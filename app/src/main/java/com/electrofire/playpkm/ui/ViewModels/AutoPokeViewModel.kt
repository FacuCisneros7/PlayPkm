package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.LocalData.PokemonEntity
import com.electrofire.playpkm.Data.Repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutoPokeViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _suggestions = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val sugerencias: StateFlow<List<PokemonEntity>> = _suggestions

    init {
        // Al iniciar, sincroniza si la base local está vacía
        viewModelScope.launch {
            if (repository.isEmpty()) {
                repository.syncPokemon()
            }
        }
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            _suggestions.value =
                if (query.isEmpty()) emptyList()
                else repository.searchPokemon(query)
        }
    }
}
