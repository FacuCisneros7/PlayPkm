package com.electrofire.playpkm.ui.ViewModels.common

/**
 * Interfaz genérica para manejar los estados de la UI de forma consistente en toda la app.
 */
sealed interface UIState<out T> {
    data object Loading : UIState<Nothing>
    data class Success<T>(val data: T) : UIState<T>
    data class Error(val message: String) : UIState<Nothing>
}
