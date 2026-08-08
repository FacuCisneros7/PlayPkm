package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.LocalData.PokemonEntity
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class AutoPokeViewModel @Inject constructor(
    private val repository: PokemonApiRepository
) : ViewModel() {

    private val _queryFlow = MutableStateFlow("")

    private val _suggestions = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val sugerencias: StateFlow<List<PokemonEntity>> = _suggestions.asStateFlow()

    init {
        // Al iniciar, sincroniza si la base local está vacía
        viewModelScope.launch {
            if (repository.isEmptyQuestion()) {
                repository.syncPokemon()
            }
        }

        viewModelScope.launch {
            _queryFlow
                .debounce(700L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    _suggestions.value = if (query.isEmpty()) {
                        emptyList()
                    } else {
                        repository.searchPokemon(query)
                    }
                }
        }
    }

    fun onQueryChanged(query: String) {
        _queryFlow.value = query
    }
}
