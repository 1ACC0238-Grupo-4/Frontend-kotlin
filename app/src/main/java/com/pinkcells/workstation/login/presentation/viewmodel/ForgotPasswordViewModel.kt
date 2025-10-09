package com.example.workstation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.pinkcells.workstation.login.data.di.ServiceLocator

class ForgotPasswordViewModel : ViewModel() {
    private val auth = ServiceLocator.authRepository()

    fun sendReset(email: String): Boolean {
        return auth.resetPassword(email)
    }
}