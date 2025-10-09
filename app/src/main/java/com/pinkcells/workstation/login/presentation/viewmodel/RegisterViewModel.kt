package com.example.workstation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.pinkcells.workstation.login.data.di.ServiceLocator

data class RegisterResult(val success: Boolean, val error: String? = null)

class RegisterViewModel : ViewModel() {
    private val auth = ServiceLocator.authRepository()

    fun register(name: String, last: String, phone: String, email: String, pass: String): RegisterResult {
        if (name.isBlank() || last.isBlank() || phone.isBlank() || email.isBlank() || pass.length < 6) {
            return RegisterResult(false, "Datos inválidos (contraseña >= 6)")
        }
        return if (auth.register(name, last, phone, email, pass)) RegisterResult(true) else RegisterResult(false, "Usuario ya existe")
    }
}