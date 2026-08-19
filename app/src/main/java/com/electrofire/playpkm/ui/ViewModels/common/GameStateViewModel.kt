package com.electrofire.playpkm.ui.ViewModels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameStateViewModel @Inject constructor() : ViewModel() {

    var respondido by mutableStateOf(false)
        private set

    fun responder() {
        respondido = true
    }

    fun reset() {
        respondido = false
    }
}
