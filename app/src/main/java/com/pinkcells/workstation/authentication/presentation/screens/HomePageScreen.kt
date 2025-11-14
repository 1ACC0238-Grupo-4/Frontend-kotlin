package com.pinkcells.workstation.authentication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pinkcells.workstation.navigation.Screen
import com.pinkcells.workstation.authentication.presentation.viewmodel.AuthViewModel

@Composable
fun HomePageScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }

    var nombreUsuario by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.obtenerDatosUsuario { usuario ->
            nombreUsuario = usuario.nombre
            tipoUsuario = usuario.tipoUsuario
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Text(
                "Bienvenido, $nombreUsuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Text(
                "Usuario: $tipoUsuario",
                fontSize = 16.sp,
                color = Color(0xFF757575)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Pantalla Principal",
                    fontSize = 20.sp,
                    color = Color(0xFF757575)
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("🏠", "Home", Screen.HomePage.route),
        BottomNavItem("📋", "Reservas", Screen.Reservas.route),
        BottomNavItem("🔍", "Buscar", Screen.Busqueda.route),
        BottomNavItem("💬", "Chats", Screen.Chats.route),
        BottomNavItem("👤", "Perfil", Screen.Perfil.route)
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF8BC34A),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Text(item.icon, fontSize = 24.sp) },
                label = { Text(item.label, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.HomePage.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    indicatorColor = Color(0xFF66BB6A),
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f)
                )
            )
        }
    }
}

data class BottomNavItem(
    val icon: String,
    val label: String,
    val route: String
)