package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Repository.UsersRepository
import com.electrofire.playpkm.Data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val repo: UsersRepository
) : ViewModel() {

    private val _states = MutableStateFlow<Map<RankingType, RankingState>>(
        RankingType.entries.associateWith { RankingState.Loading }
    )
    val states: StateFlow<Map<RankingType, RankingState>> = _states.asStateFlow()

    private val ultimaCarga = mutableMapOf<RankingType, Long>()
    private val CACHE_TIME = 2 * 60 * 60 * 1000L // 2 horas

    init {
        RankingType.entries.forEach { loadRanking(it) }
    }

    fun loadRanking(type: RankingType, force: Boolean = false) {
        val ahora = System.currentTimeMillis()
        val ultima = ultimaCarga[type] ?: 0L

        if (!force && _states.value[type] is RankingState.Success && (ahora - ultima < CACHE_TIME)) {
            return
        }

        viewModelScope.launch {
            // Solo ponemos Loading si no hay datos previos o es forzado
            if (_states.value[type] !is RankingState.Success || force) {
                updateState(type, RankingState.Loading)
            }

            val users = when (type) {
                RankingType.GENERAL -> repo.getUsersOrderedByVictories()
                RankingType.GC -> repo.getUsersOrderedByVictoriesInGC()
                RankingType.TS -> repo.getUsersOrderedByVictoriesInTS()
                RankingType.BA -> repo.getUsersOrderedByVictoriesInBA()
            }

            if (users.isNotEmpty()) {
                updateState(type, RankingState.Success(users))
                ultimaCarga[type] = ahora
            } else {
                updateState(type, RankingState.Error("No se pudieron cargar los datos"))
            }
        }
    }

    private fun updateState(type: RankingType, state: RankingState) {
        _states.value = _states.value.toMutableMap().apply {
            put(type, state)
        }
    }
}