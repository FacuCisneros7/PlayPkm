package com.electrofire.playpkm.ui.ViewModels

import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.Repository.UsersRepository
import com.electrofire.playpkm.Data.UserData
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RankingViewModel : ViewModel() {
    private val repo = UsersRepository()

    //Para que se actualize la lista en tiempo real.
    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    val users = _users.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    init {
        loadUsers()
    }

    private fun loadUsers() {
        repo.getUsersOrderedByVictories { userList ->
            _users.value = userList
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

}
