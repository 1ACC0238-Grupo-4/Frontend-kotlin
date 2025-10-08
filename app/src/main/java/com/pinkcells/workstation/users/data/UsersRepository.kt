package com.pinkcells.workstation.users.data

import android.util.Log
import com.pinkcells.workstation.users.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UsersRepository {

    private val _usersFlow = MutableStateFlow<List<User>>(emptyList())
    val usersFlow: StateFlow<List<User>> = _usersFlow.asStateFlow();

    suspend fun fetchUserById(id: String): User? {
        return try {
            val response = RetrofitInstance.api.getUserById(id)
            if (response.isSuccessful) {
                val user = response.body()
                user
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUser(user: User): User? {
        Log.d("UsersRepository", "updateUser(${user.id}) - Actualizando usuario: ${user.id}")

        return try {
            if (user.id == null) {
                Log.e("UsersRepository", "updateUser: El ID del usuario es nulo.")
                return null
            }

            val response = RetrofitInstance.api.updateUser(user.id, user)
            Log.d("UsersRepository", "Response code: ${response.code()}")

            if (response.isSuccessful){
                val updatedUser = response.body()
                Log.e("UsersRepository", "Usuario actualizado: $updatedUser")

                updatedUser
            }else {
                val errorBody = response.errorBody()?.string()
                Log.e("UsersRepository", "Error actualizando usuario: ${response.code()} - $errorBody")
                null
            }
        }catch (e: Exception){
            Log.e("UsersRepository", "Excepción al actualizar usuario: ${e.message}", e)
            null
        }

    }

    suspend fun deleteUser(id: String): Boolean {
        Log.d("UsersRepository", "deleteUser($id) - Eliminando usuario")

        return try {
            val response = RetrofitInstance.api.deleteUser(id)
            Log.d("UsersRepository", "Response code: ${response.code()}")

            if (response.isSuccessful){
                Log.d("UsersRepository", "Usuario eliminado correctamente")
                true
            }else {
                val errorBody = response.errorBody()?.string()
                Log.e("UsersRepository", "Error eliminando usuario: ${response.code()} - $errorBody")
                false
            }
        }catch (e: Exception){
            Log.e("UsersRepository", "Excepción al eliminar usuario: ${e.message}", e)
            false
        }

    }
}