package com.electrofire.playpkm.ui.ViewModels.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.ShopData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor() : ViewModel() {

    var shopData by mutableStateOf(ShopData())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val firestore = FirebaseFirestore.getInstance()

    init {
        loadShopItems()
    }

    fun loadShopItems() {
        isLoading = true
        // Cambiamos a cargar un único documento para ahorrar lecturas
        firestore.collection("Shop")
            .document("ShopData")
            .get()
            .addOnSuccessListener { result ->
                try {
                    if (result.exists()) {
                        shopData = result.toObject(ShopData::class.java) ?: ShopData()
                        Log.d("ShopVM", "Datos de tienda cargados correctamente")
                    }
                } catch (e: Exception) {
                    Log.e("ShopVM", "Error mapeando ShopData", e)
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.e("ShopVM", "Error cargando tienda", e)
                isLoading = false
            }
    }
}
