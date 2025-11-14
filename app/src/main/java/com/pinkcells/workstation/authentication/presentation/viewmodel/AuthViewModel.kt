package com.pinkcells.workstation.authentication.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel

data class Usuario(
    val nombre: String = "",
    val apellido: String = "",
    val dni: String = "",
    val email: String = "",
    val celular: String = "",
    val contraseña: String = "",
    val tipoUsuario: String = ""
)

class AuthViewModel(private val context: Context) : ViewModel() {

    private val prefs: SharedPreferences = context.getSharedPreferences("WorkstationPrefs", Context.MODE_PRIVATE)

    fun validateNombre(nombre: String): Boolean {
        return nombre.length >= 3
    }

    fun validateApellido(apellido: String): Boolean {
        return apellido.length >= 3
    }

    fun validateEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    fun validateDNI(dni: String): Boolean {
        return dni.length == 8 && dni.all { it.isDigit() }
    }

    fun validateCelular(celular: String): Boolean {
        return celular.length == 9 && celular.all { it.isDigit() }
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
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
        val emailExiste = prefs.contains("usuario_$email")

        if (emailExiste) {
            onResult(false, "Este email ya está registrado")
            return
        }

        val editor = prefs.edit()
        editor.putString("usuario_${email}_nombre", nombre)
        editor.putString("usuario_${email}_apellido", apellido)
        editor.putString("usuario_${email}_dni", dni)
        editor.putString("usuario_${email}_email", email)
        editor.putString("usuario_${email}_celular", celular)
        editor.putString("usuario_${email}_contraseña", contraseña)
        editor.putString("usuario_${email}_tipoUsuario", tipoUsuario)
        editor.putBoolean("usuario_$email", true)

        editor.putString("sesion_actual_email", email)
        editor.putBoolean("sesion_activa", true)

        editor.apply()

        onResult(true, "Usuario registrado exitosamente")
    }

    fun iniciarSesion(email: String, contraseña: String, onResult: (Boolean, String) -> Unit) {
        val usuarioExiste = prefs.getBoolean("usuario_$email", false)

        if (!usuarioExiste) {
            onResult(false, "Usuario no encontrado")
            return
        }

        val contraseñaGuardada = prefs.getString("usuario_${email}_contraseña", "")

        if (contraseña != contraseñaGuardada) {
            onResult(false, "Contraseña incorrecta")
            return
        }

        val editor = prefs.edit()
        editor.putString("sesion_actual_email", email)
        editor.putBoolean("sesion_activa", true)
        editor.apply()

        onResult(true, "Inicio de sesión exitoso")
    }

    fun recuperarContraseña(email: String, onResult: (Boolean, String) -> Unit) {
        val usuarioExiste = prefs.getBoolean("usuario_$email", false)

        if (!usuarioExiste) {
            onResult(false, "No existe una cuenta con este email")
            return
        }

        onResult(true, "Se ha enviado un correo a $email con instrucciones para recuperar tu contraseña")
    }

    fun obtenerDatosUsuario(onResult: (Usuario) -> Unit) {
        val email = prefs.getString("sesion_actual_email", "") ?: ""

        if (email.isEmpty()) {
            onResult(Usuario())
            return
        }

        val usuario = Usuario(
            nombre = prefs.getString("usuario_${email}_nombre", "") ?: "",
            apellido = prefs.getString("usuario_${email}_apellido", "") ?: "",
            dni = prefs.getString("usuario_${email}_dni", "") ?: "",
            email = prefs.getString("usuario_${email}_email", "") ?: "",
            celular = prefs.getString("usuario_${email}_celular", "") ?: "",
            contraseña = prefs.getString("usuario_${email}_contraseña", "") ?: "",
            tipoUsuario = prefs.getString("usuario_${email}_tipoUsuario", "") ?: ""
        )

        onResult(usuario)
    }

    fun actualizarDatosUsuario(
        nombre: String,
        apellido: String,
        celular: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val email = prefs.getString("sesion_actual_email", "") ?: ""

        if (email.isEmpty()) {
            onResult(false, "No hay sesión activa")
            return
        }

        val editor = prefs.edit()
        editor.putString("usuario_${email}_nombre", nombre)
        editor.putString("usuario_${email}_apellido", apellido)
        editor.putString("usuario_${email}_celular", celular)
        editor.apply()

        onResult(true, "Perfil actualizado exitosamente")
    }

    fun cerrarSesion() {
        val editor = prefs.edit()
        editor.remove("sesion_actual_email")
        editor.putBoolean("sesion_activa", false)
        editor.apply()
    }

    fun haySesionActiva(): Boolean {
        return prefs.getBoolean("sesion_activa", false)
    }

    fun obtenerTipoUsuario(): String {
        val email = prefs.getString("sesion_actual_email", "") ?: ""
        return prefs.getString("usuario_${email}_tipoUsuario", "") ?: ""
    }
}