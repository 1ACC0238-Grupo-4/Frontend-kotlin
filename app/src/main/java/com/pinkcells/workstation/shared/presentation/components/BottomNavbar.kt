package com.pinkcells.workstation.shared.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme
//import com.pinkcells.workstation.shared.presentation.components.RoutesBN


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
BottomNavItem(icon = Icons.Default.Home, label = "Home", route = RoutesBN.HOME),
        BottomNavItem(icon = Icons.Default.DateRange, label = "Reservations", route = RoutesBN.OFFICES),
        //[3]Agregar la pantalla al BottomNavigation
        BottomNavItem(icon = Icons.Default.Search, label = "Search", route = RoutesBN.SEARCH_OFFICE),
        BottomNavItem(icon = Icons.Default.Email, label = "Chats", route = "chats"),
        BottomNavItem(icon = Icons.Default.Person, label = "Profile", route = RoutesBN.PROFILE)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF5BB318),
        modifier = Modifier.height(80.dp)
    ) {
items.forEach { item ->
            val selected = when (item.route) {
                RoutesBN.HOME -> currentRoute == RoutesBN.HOME || currentRoute == null
                RoutesBN.OFFICES -> currentRoute == RoutesBN.OFFICES || (currentRoute?.startsWith("office/") == true) || currentRoute == RoutesBN.OFFICE_CREATE
                RoutesBN.CHATS -> currentRoute == RoutesBN.CHATS
                RoutesBN.PROFILE -> currentRoute == RoutesBN.PROFILE || currentRoute == null
                else -> currentRoute == item.route
            }

            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) Color(0xFFE8F36C)
                                else Color(0xFF9FD857)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                selected = selected,
                onClick = {
                    // [4] Agregar SEARCH_OFFICE como ruta permitida
                    if (item.route in listOf(RoutesBN.HOME, RoutesBN.OFFICES, RoutesBN.CHATS, RoutesBN.PROFILE, RoutesBN.SEARCH_OFFICE)) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    } else {
                        // Not implemented yet
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class BottomNavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppRootPreview() {
    WorkstationTheme {
        AppRoot()
    }
}
