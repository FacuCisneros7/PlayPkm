package com.electrofire.playpkm.ui.ViewModels

import com.electrofire.playpkm.Data.UserData

enum class RankingType {
    GENERAL, GC, TS, BA
}

sealed interface RankingState {
    data object Loading : RankingState
    data class Success(val users: List<UserData>) : RankingState
    data class Error(val message: String) : RankingState
}