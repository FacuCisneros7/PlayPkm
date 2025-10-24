package com.electrofire.playpkm.ui.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.electrofire.playpkm.Data.Repository.UsersRepository
import com.electrofire.playpkm.Data.UserData

class RankingViewModel : ViewModel() {
    private val repo = UsersRepository()

    private val _users = mutableStateOf<List<UserData>>(emptyList())
    var users by _users  // ahora `users` es List<UserData>
        private set

    init {
        loadUsers()
    }

    private fun loadUsers() {
        repo.getUsersOrderedByVictories { userList ->
            users = userList
        }
    }
}
