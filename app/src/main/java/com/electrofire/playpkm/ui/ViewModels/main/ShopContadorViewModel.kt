package com.electrofire.playpkm.ui.ViewModels.main

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
class ShopContadorViewModel @Inject constructor(
    private val timeRepository: TimeRepository
) : ViewModel() {

    private val _timeLeft = MutableStateFlow("00:00:00")
    val timeLeft: StateFlow<String> = _timeLeft

    fun startCountdown() {
        viewModelScope.launch {
            val horaServidor = timeRepository.obtenerHoraServidor() ?: java.util.Date()

            flow {
                while (true) {
                    val now = horaServidor.time + (System.currentTimeMillis() - horaServidor.time)

                    val nextMonday = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                        timeInMillis = now
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                        val dayOfWeek = get(Calendar.DAY_OF_WEEK)
                        val daysUntilMonday = if (dayOfWeek == Calendar.MONDAY) {
                            7
                        } else {
                            (Calendar.MONDAY - dayOfWeek + 7) % 7
                        }
                        if (daysUntilMonday == 0) add(Calendar.DAY_OF_MONTH, 7)
                        else add(Calendar.DAY_OF_MONTH, daysUntilMonday)
                    }.timeInMillis

                    val diff = nextMonday - now
                    val days = diff / 1000 / 86400
                    val hours = (diff / 1000 / 3600) % 24
                    val minutes = (diff / 1000 / 60) % 60
                    val seconds = (diff / 1000) % 60

                    val timeString = if (days > 0) {
                        "%d d %02d:%02d:%02d".format(days, hours, minutes, seconds)
                    } else {
                        "%02d:%02d:%02d".format(hours, minutes, seconds)
                    }

                    emit(timeString)
                    delay(1000)
                }
            }.collect { value ->
                _timeLeft.value = value
            }
        }
    }
}
