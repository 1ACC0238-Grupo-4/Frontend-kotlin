package com.pinkcells.workstation.authentication.data.repository

import com.google.gson.Gson
import com.pinkcells.workstation.authentication.data.api.RetrofitClient
import com.pinkcells.workstation.authentication.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {

    private val api = RetrofitClient.authApi
    private val gson = Gson()

    private fun hashPassword(password: String): String {
        return password
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(
                    email = email,
                    passwordHash = hashPassword(password)
                )
                val response = api.login(request)

                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    println("Login Response Body: $responseBody")

                    if (!responseBody.isNullOrBlank()) {
                        val token = responseBody.trim()

                        val userId = try {
                            decodeJwtUserId(token)
                        } catch (e: Exception) {
                            println("No se pudo decodificar userId del token: ${e.message}")
                            1
                        }

                        val loginResponse = LoginResponse(
                            token = token,
                            userId = userId,
                            message = "Login exitoso"
                        )
                        Result.success(loginResponse)
                    } else {
                        Result.failure(Exception("Respuesta vacía del servidor"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("Login Error Body: $errorBody")

                    val errorMessage = when(response.code()) {
                        400 -> errorBody ?: "Credenciales inválidas"
                        401 -> "Email o contraseña incorrectos"
                        404 -> "Usuario no encontrado"
                        else -> errorBody ?: "Error al iniciar sesión (${response.code()})"
                    }
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                println("Login Exception: ${e.message}")
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    suspend fun signUp(
        firstName: String,
        lastName: String,
        dni: String,
        email: String,
        phoneNumber: String,
        password: String,
        role: Int = 1
    ): Result<SignUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = SignUpRequest(
                    firstName = firstName,
                    lastName = lastName,
                    dni = dni,
                    phoneNumber = phoneNumber,
                    email = email,
                    passwordHash = hashPassword(password),
                    role = role
                )
                val response = api.signUp(request)

                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    println("SignUp Response Body: $responseBody")

                    val message = responseBody ?: "Usuario creado exitosamente"

                    val signUpResponse = SignUpResponse(
                        message = message,
                        userId = "temp_${System.currentTimeMillis()}",
                        success = true
                    )
                    Result.success(signUpResponse)
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("SignUp Error Body: $errorBody")

                    val errorMessage = when(response.code()) {
                        400 -> errorBody ?: "Datos inválidos"
                        409 -> "El usuario ya existe"
                        else -> errorBody ?: "Error al registrar (${response.code()})"
                    }
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                println("SignUp Exception: ${e.message}")
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    private fun decodeJwtUserId(token: String): Int {
        try {
            val parts = token.split(".")
            if (parts.size != 3) {
                throw Exception("Token JWT inválido")
            }

            val payload = parts[1]
            val decodedBytes = android.util.Base64.decode(payload, android.util.Base64.URL_SAFE)
            val decodedString = String(decodedBytes)

            println("JWT Payload decodificado: $decodedString")

            val payloadJson = gson.fromJson(decodedString, Map::class.java)

            val userId = when {
                payloadJson.containsKey("userId") -> {
                    (payloadJson["userId"] as? Number)?.toInt()
                }
                payloadJson.containsKey("sub") -> {
                    (payloadJson["sub"] as? Number)?.toInt()
                }
                payloadJson.containsKey("id") -> {
                    (payloadJson["id"] as? Number)?.toInt()
                }
                else -> null
            }

            return userId ?: 1
        } catch (e: Exception) {
            println("Error decodificando JWT: ${e.message}")
            return 1
        }
    }

    suspend fun fetchUserProfile(userId: Int): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                println("Buscando usuario con userId: $userId")
                val response = api.getAllUsers()

                if (response.isSuccessful && response.body() != null) {
                    val allUsers = response.body()!!
                    println("Total usuarios recibidos: ${allUsers.size}")
                    allUsers.forEachIndexed { index, user ->
                        println("Usuario $index: userId=${user.userId}, firstName=${user.firstName}, lastName=${user.lastName}, email=${user.email}")
                    }
                    val user = allUsers.find { it.userId == userId }

                    if (user != null) {
                        println("Usuario encontrado: ${user.firstName} ${user.lastName}")
                        Result.success(user)
                    } else {
                        println("Usuario con userId=$userId no encontrado en la lista")

                        println("Nota: No se encontró el usuario. Verifica que el userId del JWT coincida con los datos del API")
                        Result.failure(Exception("Usuario con ID $userId no encontrado"))
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Error desconocido"
                    println("Error en la respuesta del API: ${response.code()} - $error")
                    Result.failure(Exception("Error al obtener perfil: ${response.code()} - $error"))
                }
            } catch (e: Exception) {
                println("Excepción al obtener perfil: ${e.message}")
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

    suspend fun getAllUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getAllUsers()

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al obtener usuarios"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    suspend fun fetchUserByEmail(email: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                println("Buscando usuario por email: $email")
                val response = api.getAllUsers()

                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.find {
                        it.email.equals(email, ignoreCase = true)
                    }

                    if (user != null) {
                        println("Usuario encontrado por email: ${user.firstName} ${user.lastName}")
                        Result.success(user)
                    } else {
                        println("Usuario con email=$email no encontrado")
                        Result.failure(Exception("Usuario no encontrado"))
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Error desconocido"
                    Result.failure(Exception("Error al obtener perfil: ${response.code()} - $error"))
                }
            } catch (e: Exception) {
                println("Excepción al buscar usuario por email: ${e.message}")
                Result.failure(e)
            }
        }
    }
}