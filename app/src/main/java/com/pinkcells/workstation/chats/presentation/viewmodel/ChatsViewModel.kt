package com.pinkcells.workstation.chats.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcells.workstation.chats.data.UserRepository
import com.pinkcells.workstation.chats.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatsViewModel : ViewModel() {
    private val _chats = MutableStateFlow<List<User>>(emptyList())
    val chats: StateFlow<List<User>> = _chats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchChats()
    }

    fun fetchChats() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            Log.d("ChatsViewModel", "Starting to fetch chats...")
            try {
                val response = UserRepository.fetchUsers()

                Log.d("ChatsViewModel", "API Response received: $response")
                Log.d("ChatsViewModel", "Number of chats: ${response.size}")
                _chats.value = response
                Log.d("ChatsViewModel", "Chats updated in StateFlow: ${_chats.value}")

            } catch (e: Exception){
                Log.e("ChatsViewModel", "Error fetching chats: ${e.message}", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
                Log.d("ChatsViewModel", "Loading finished. isLoading = ${_isLoading.value}")
            }
        }
    }
}