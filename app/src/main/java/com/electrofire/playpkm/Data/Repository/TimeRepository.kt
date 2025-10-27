package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.NetworkData.TimeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class TimeRepository @Inject constructor() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://google.com/")
        .build()
    private val service = retrofit.create(TimeApi::class.java)

    suspend fun obtenerHoraServidor(): Date? {
        val response = service.getServerTime()
        val dateHeader = response.headers()["Date"]
        return dateHeader?.let {
            val formato = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
            formato.timeZone = TimeZone.getTimeZone("GMT")
            formato.parse(it)
        }
    }

    fun obtenerHoraServidorDos(callback: (Date?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val date = obtenerHoraServidor() // tu suspend function
            withContext(Dispatchers.Main) {
                callback(date)
            }
        }
    }
}