package com.electrofire.playpkm.ui.ViewModels.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.ShopItem
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor() : ViewModel() {

    var shopItems by mutableStateOf<List<ShopItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val firestore = FirebaseFirestore.getInstance()

    init {
        loadShopItems()
    }

    fun loadShopItems() {
        isLoading = true
        firestore.collection("Shop")
            .get()
            .addOnSuccessListener { result ->
                try {
                    shopItems = result.toObjects(ShopItem::class.java)
                    Log.d("ShopVM", "Items cargados: ${shopItems.size}")
                } catch (e: Exception) {
                    Log.e("ShopVM", "Error mapeando items", e)
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.e("ShopVM", "Error cargando tienda", e)
                isLoading = false
            }
    }
}
