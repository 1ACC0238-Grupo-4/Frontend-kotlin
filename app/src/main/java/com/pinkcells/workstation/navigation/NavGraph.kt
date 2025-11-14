package com.pinkcells.workstation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinkcells.workstation.authentication.presentation.screens.LoadingScreen
import com.pinkcells.workstation.authentication.presentation.screens.LogInScreen
import com.pinkcells.workstation.authentication.presentation.screens.RecoverPasswordScreen
import com.pinkcells.workstation.authentication.presentation.screens.Register1Screen
import com.pinkcells.workstation.authentication.presentation.screens.Register2Screen
import com.pinkcells.workstation.shared.presentation.components.AppRoot

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
            AppRoot()
        }
    }
}