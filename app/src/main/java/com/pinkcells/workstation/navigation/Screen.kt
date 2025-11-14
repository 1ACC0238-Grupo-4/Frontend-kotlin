package com.pinkcells.workstation.navigation

sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object IniciarSesion : Screen("iniciar_sesion")
    object Registro1 : Screen("registro1")
    object Registro2 : Screen("registro2")
    object RecuperarContraseña : Screen("recuperar_contraseña")
    object HomePage : Screen("home_page")
}