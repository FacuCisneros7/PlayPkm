package com.electrofire.playpkm.ui.ViewModels.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.RankingCache
import com.electrofire.playpkm.Data.Repository.TimeRepository
import com.electrofire.playpkm.Data.Repository.UsersRepository
import com.electrofire.playpkm.Data.UserData
import com.electrofire.playpkm.ui.ViewModels.common.RankingType
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val repo: UsersRepository
) : ViewModel() {

    private val _states = MutableStateFlow<Map<RankingType, UIState<List<UserData>>>>(
        RankingType.entries.associateWith { UIState.Loading }
    )
    val states: StateFlow<Map<RankingType, UIState<List<UserData>>>> = _states.asStateFlow()

    private val timeRepository = TimeRepository()
    private var isAlreadyLoading = false

    init {
        initialLoad()
    }

    private fun initialLoad() {
        if (isAlreadyLoading) return
        isAlreadyLoading = true

        viewModelScope.launch {
            Log.d("RankingCache", "--- CONSULTANDO CACHÉ GLOBAL ---")
            val cache = repo.getRankingCache()
            
            // Si ya tenemos datos, los mostramos de inmediato (UX rápida)
            cache?.let { updateAllStates(it) }

            timeRepository.obtenerHoraServidorDos { serverDate ->
                if (serverDate == null) {
                    Log.w("RankingCache", "Fallo de conexión horaria. Manteniendo datos actuales.")
                    return@obtenerHoraServidorDos
                }

                viewModelScope.launch {
                    val nowUTC = serverDate.toInstant().atZone(ZoneOffset.UTC).toLocalDate()
                    val lastUpdatedDate = cache?.lastUpdated?.toDate()
                    
                    // Si el documento existe pero no tiene fecha (pendiente de sync), 
                    // NO refrescamos. Esperamos a la próxima vez.
                    if (cache != null && cache.lastUpdated == null) {
                        Log.d("RankingCache", "Documento en sync local. Evitando refresco preventivo.")
                        return@launch
                    }

                    val lastUpdatedUTC = lastUpdatedDate?.toInstant()?.atZone(ZoneOffset.UTC)?.toLocalDate()

                    Log.d("RankingCache", "Día Servidor: $nowUTC | Día Caché: $lastUpdatedUTC")

                    val needsRefresh = when {
                        cache == null -> true
                        lastUpdatedUTC != null && lastUpdatedUTC != nowUTC -> true
                        else -> false
                    }

                    if (needsRefresh) {
                        Log.w("RankingCache", ">>> REFRESCANDO RANKING (120 reads) <<<")
                        val newCache = repo.refreshAndSaveRankingCache()
                        updateAllStates(newCache)
                    } else {
                        Log.d("RankingCache", ">>> CACHÉ VÁLIDO. 0 lecturas de usuarios. <<<")
                    }
                }
            }
        }
    }

    private fun updateAllStates(cache: RankingCache) {
        val newMap = mapOf(
            RankingType.GENERAL to UIState.Success(cache.general),
            RankingType.WEEKLY to UIState.Success(cache.weekly),
            RankingType.GC to UIState.Success(cache.goodChoice),
            RankingType.TS to UIState.Success(cache.thousandShadows),
            RankingType.BA to UIState.Success(cache.beforeAfter)
        )
        _states.value = newMap
    }

}
