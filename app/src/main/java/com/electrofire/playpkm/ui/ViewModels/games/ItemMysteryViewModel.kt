package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.ItemApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ItemMysteryViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<ItemApi>>(UIState.Loading)
    val state: StateFlow<UIState<ItemApi>> = _state

    init {
        loadItem()
    }

    fun loadItem() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                val item = repo.obtenerItemRandom()
                if (item != null) {
                    _state.value = UIState.Success(item)
                } else {
                    _state.value = UIState.Error("No se pudo obtener un objeto.")
                }
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("ITEM_GAME_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado.")
            }
        }
    }

    fun verifyAnswer(item: ItemApi, answer: String): Boolean {
        fun String.normalizar(): String =
            java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
                .replace("\\p{Mn}+".toRegex(), "")
                .replace(" ", "") // Elimina TODOS los espacios
                .lowercase()
                .trim()

        return item.name.normalizar() == answer.normalizar()
    }
}
