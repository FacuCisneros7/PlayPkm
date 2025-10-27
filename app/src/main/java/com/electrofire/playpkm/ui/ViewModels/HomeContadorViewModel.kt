package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.Repository.TimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class HomeContadorViewModel @Inject constructor(
    private val timeRepository: TimeRepository
) : ViewModel() {

    // Flow que emite el tiempo restante como string
    private val _timeLeft = MutableStateFlow("00:00:00")
    val timeLeft: StateFlow<String> = _timeLeft

    fun startCountdown() {
        viewModelScope.launch {
            val horaServidor = timeRepository.obtenerHoraServidor() ?: return@launch

            // Creamos un flow que emite cada segundo
            flow {
                while (true) {
                    val now = horaServidor.time + (System.currentTimeMillis() - horaServidor.time)
                    val endOfDay = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                        timeInMillis = now
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        add(Calendar.DAY_OF_MONTH, 1)
                    }.timeInMillis

                    val diff = endOfDay - now
                    val hours = diff / 1000 / 3600
                    val minutes = (diff / 1000 / 60) % 60
                    val seconds = (diff / 1000) % 60

                    emit("%02d:%02d:%02d".format(hours, minutes, seconds)) // emitimos cada segundo
                    delay(1000)
                }
            }.collect { value ->
                _timeLeft.value = value // actualizamos el StateFlow
            }
        }
    }
}
