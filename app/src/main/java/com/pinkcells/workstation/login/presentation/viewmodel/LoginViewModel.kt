package com.example.workstation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.pinkcells.workstation.login.data.di.ServiceLocator

data class AuthResult(val success: Boolean, val userName: String? = null, val error: String? = null)

class LoginViewModel : ViewModel() {
    private val auth = ServiceLocator.authRepository()

    fun login(email: String, password: String): AuthResult {
        if (email.isBlank() || password.isBlank()) return AuthResult(false, error = "Completa los campos")
        return if (auth.login(email, password)) AuthResult(true, userName = email.substringBefore("@"))
        else AuthResult(false, error = "Credenciales inválidas")
    }
}