package com.electrofire.playpkm.ui.ViewModels.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.LocalData.AbilityEntity
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
class AutoAbilityViewModel @Inject constructor(
    private val repository: PokemonApiRepository
) : ViewModel() {

    private val _queryFlow = MutableStateFlow("")

    private val _suggestions = MutableStateFlow<List<AbilityEntity>>(emptyList())
    val sugerencias: StateFlow<List<AbilityEntity>> = _suggestions.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSyncAbilities()
        }

        viewModelScope.launch {
            _queryFlow
                .debounce(300L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    _suggestions.value = if (query.isEmpty()) {
                        emptyList()
                    } else {
                        repository.searchAbility(query)
                    }
                }
        }
    }

    fun onQueryChanged(query: String) {
        _queryFlow.value = query
    }
}
