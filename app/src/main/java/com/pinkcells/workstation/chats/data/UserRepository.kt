package com.pinkcells.workstation.chats.data

import android.util.Log
import com.pinkcells.workstation.chats.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserRepository {
    private val _userFlow = MutableStateFlow<List<User>>(emptyList())
    val usersFlow: StateFlow<List<User>> = _userFlow.asStateFlow()

    suspend fun fetchUsers(): List<User> {
        Log.d("UserRepository", "fetchUsers() - Iniciando llamada a la API...")

        return try {
            val response = RetrofitInstanceUser.api.getUsers()

            Log.d("UsersRepository", "Response code: ${response.code()}")
            Log.d("UsersRepository", "Response isSuccessful: ${response.isSuccessful}")
            Log.d("UsersRepository", "Response body: ${response.body()}")

            if (response.isSuccessful) {
                val users = response.body() ?: emptyList()
                Log.d("UsersRepository", "Oficina recibida  ${users.size}")

                _userFlow.value = users
                users
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UsersRepository", "Errror ${response.code()}: $errorBody")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("UsersRepository", "No se llamo a la API ${e.message}", e)
            Log.e("UsersRepository", "Stack trace: ${e.stackTraceToString()}")
            emptyList()
        }
    }
}