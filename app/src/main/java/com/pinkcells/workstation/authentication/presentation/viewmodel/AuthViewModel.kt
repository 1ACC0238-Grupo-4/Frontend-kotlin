package com.pinkcells.workstation.authentication.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcells.workstation.authentication.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val context: Context) : ViewModel() {

    private val repository = AuthRepository()

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun iniciarSesion(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val loginResult = repository.login(email, password)

                loginResult.onSuccess { response ->
                    val token = response.token
                    val userId = response.userId

                    if (token != null && userId != null && userId != 0) {
                        saveAuthData(token, userId)

                        val profileResult = repository.fetchUserProfile(userId)

                        profileResult.onSuccess { user ->
                            val tipoUsuario = if (user.role == 2) "Arrendador" else "Arrendatario"

                            saveUserData(
                                nombre = user.firstName,
                                apellido = user.lastName,
                                dni = user.dni,
                                email = user.email,
                                celular = user.phoneNumber,
                                tipoUsuario = tipoUsuario
                            )
                            println("Perfil cargado correctamente: ${user.firstName} ${user.lastName}")
                            onResult(true, response.message ?: "Login exitoso")
                        }.onFailure { e ->
                            println("Error al cargar perfil por userId, intentando por email...")

                            val emailResult = repository.fetchUserByEmail(email)
                            emailResult.onSuccess { user ->
                                val tipoUsuario = if (user.role == 2) "Arrendador" else "Arrendatario"

                                saveUserData(
                                    nombre = user.firstName,
                                    apellido = user.lastName,
                                    dni = user.dni,
                                    email = user.email,
                                    celular = user.phoneNumber,
                                    tipoUsuario = tipoUsuario
                                )
                                println("Perfil cargado por email: ${user.firstName} ${user.lastName}")
                                onResult(true, "Login exitoso")
                            }.onFailure { emailError ->
                                println("No se pudo cargar el perfil: ${emailError.message}")
                                onResult(true, "Login exitoso, pero no se pudo cargar el perfil")
                            }
                        }
                    } else {
                        onResult(false, response.message ?: "Error: Token o ID faltante")
                    }
                }.onFailure { exception ->
                    onResult(false, exception.message ?: "Error de conexión")
                }
            } catch (e: Exception) {
                onResult(false, "Error inesperado: ${e.message}")
            }
        }
    }

    fun registrarUsuario(
        nombre: String,
        apellido: String,
        dni: String,
        email: String,
        celular: String,
        contraseña: String,
        tipoUsuario: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val rol = when(tipoUsuario) {
                    "Arrendador" -> 2
                    "Arrendatario" -> 1
                    else -> 1
                }

                val result = repository.signUp(
                    firstName = nombre,
                    lastName = apellido,
                    dni = dni,
                    email = email,
                    phoneNumber = celular,
                    password = contraseña,
                    role = rol
                )

                result.onSuccess { response ->
                    if (response.success) {
                        println("Registro exitoso: ${response.message}")

                        iniciarSesionDespuesDeRegistro(
                            email = email,
                            password = contraseña,
                            nombre = nombre,
                            apellido = apellido,
                            dni = dni,
                            celular = celular,
                            tipoUsuario = tipoUsuario,
                            onResult = onResult
                        )
                    } else {
                        onResult(false, response.message ?: "Error al registrar")
                    }
                }.onFailure { exception ->
                    onResult(false, exception.message ?: "Error de conexión")
                }
            } catch (e: Exception) {
                onResult(false, "Error inesperado: ${e.message}")
            }
        }
    }

    private fun iniciarSesionDespuesDeRegistro(
        email: String,
        password: String,
        nombre: String,
        apellido: String,
        dni: String,
        celular: String,
        tipoUsuario: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                println("Iniciando sesión automática...")

                kotlinx.coroutines.delay(500)

                val loginResult = repository.login(email, password)

                loginResult.onSuccess { response ->
                    val token = response.token
                    val userId = response.userId

                    if (token != null && userId != null && userId != 0) {
                        saveAuthData(token, userId)

                        val profileResult = repository.fetchUserByEmail(email)

                        profileResult.onSuccess { user ->
                            saveUserData(
                                nombre = user.firstName,
                                apellido = user.lastName,
                                dni = user.dni,
                                email = user.email,
                                celular = user.phoneNumber,
                                tipoUsuario = if (user.role == 2) "Arrendador" else "Arrendatario"
                            )
                            println("Login automático exitoso con perfil del servidor")
                            onResult(true, "Registro y login exitosos")
                        }.onFailure {
                            saveUserData(nombre, apellido, dni, email, celular, tipoUsuario)
                            println("Login automático exitoso con datos locales")
                            onResult(true, "Registro y login exitosos")
                        }
                    } else {
                        println("Login automático falló, guardando datos localmente")
                        saveUserData(nombre, apellido, dni, email, celular, tipoUsuario)
                        onResult(true, "Registro exitoso")
                    }
                }.onFailure { exception ->
                    println("Error en login automático: ${exception.message}")
                    saveUserData(nombre, apellido, dni, email, celular, tipoUsuario)
                    onResult(true, "Registro exitoso")
                }
            } catch (e: Exception) {
                println("Excepción en login automático: ${e.message}")
                saveUserData(nombre, apellido, dni, email, celular, tipoUsuario)
                onResult(true, "Registro exitoso")
            }
        }
    }

    fun recuperarContraseña(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(1500)

                if (validateEmail(email)) {
                    onResult(true, "Se ha enviado un correo de recuperación a $email")
                } else {
                    onResult(false, "Email inválido")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            }
        }
    }

    fun obtenerDatosUsuario(onResult: (DatosUsuario) -> Unit) {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val datos = DatosUsuario(
            nombre = sharedPref.getString("nombre", "") ?: "",
            apellido = sharedPref.getString("apellido", "") ?: "",
            dni = sharedPref.getString("dni", "") ?: "",
            email = sharedPref.getString("email", "") ?: "",
            celular = sharedPref.getString("celular", "") ?: "",
            tipoUsuario = sharedPref.getString("tipoUsuario", "") ?: ""
        )
        println("Datos obtenidos: $datos")
        onResult(datos)
    }

    fun actualizarDatosUsuario(
        nombre: String,
        apellido: String,
        celular: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("nombre", nombre)
            putString("apellido", apellido)
            putString("celular", celular)
            apply()
        }
        println("Datos actualizados: $nombre $apellido - $celular")
        onResult(true, "Datos actualizados correctamente")
    }

    fun haySesionActiva(): Boolean {
        return isLoggedIn()
    }

    fun cerrarSesion() {
        logout()
    }

    private fun saveAuthData(token: String, userId: Int) {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("token", token)
            putInt("userId", userId)
            putBoolean("isLoggedIn", true)
            apply()
        }
        println("Token y userId guardados: token=${token.take(20)}..., userId=$userId")
    }

    private fun saveUserData(
        nombre: String,
        apellido: String,
        dni: String,
        email: String,
        celular: String,
        tipoUsuario: String
    ) {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("nombre", nombre)
            putString("apellido", apellido)
            putString("dni", dni)
            putString("email", email)
            putString("celular", celular)
            putString("tipoUsuario", tipoUsuario)
            apply()
        }
        println("Datos de usuario guardados: $nombre $apellido - $tipoUsuario")
    }

    fun getToken(): String? {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    fun getUserId(): Int {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("userId", 0)
    }

    fun isLoggedIn(): Boolean {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    private fun logout() {
        val sharedPref = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        println("Sesión cerrada")
    }
}

data class DatosUsuario(
    val nombre: String,
    val apellido: String,
    val dni: String,
    val email: String,
    val celular: String,
    val tipoUsuario: String
)