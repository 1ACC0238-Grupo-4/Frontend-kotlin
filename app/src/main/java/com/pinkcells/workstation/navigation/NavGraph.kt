package com.pinkcells.workstation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pinkcells.workstation.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Loading.route
    ) {

        composable(route = Screen.Loading.route) {
            LoadingScreen(navController = navController)
        }

        composable(route = Screen.IniciarSesion.route) {
            LogInScreen(navController = navController)
        }

        composable(route = Screen.Registro1.route) {
            Register1Screen(navController = navController)
        }

        composable(route = Screen.Registro2.route) {
            Register2Screen(navController = navController)
        }

        composable(route = Screen.RecuperarContraseña.route) {
            RecoverPasswordScreen(navController = navController)
        }

        composable(route = Screen.HomePage.route) {
            HomePageScreen(navController = navController)
        }

        composable(route = Screen.Perfil.route) {
            PerfilScreen(navController = navController)
        }

        composable(route = Screen.Reservas.route) {
            ReservasScreen(navController = navController)
        }

        composable(route = Screen.Busqueda.route) {
            BusquedaScreen(navController = navController)
        }

        composable(route = Screen.Chats.route) {
            ChatsScreen(navController = navController)
        }
    }
}