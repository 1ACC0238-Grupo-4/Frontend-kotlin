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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.pinkcells.workstation.offices.presentation.screens.OfficesPage
import com.pinkcells.workstation.shared.presentation.screens.HomePage
import com.pinkcells.workstation.offices.presentation.screens.OfficeDetailPage
import androidx.compose.ui.tooling.preview.Preview
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
BottomNavItem(icon = Icons.Default.Home, label = "Home", route = RoutesBN.HOME),
        BottomNavItem(icon = Icons.Default.DateRange, label = "Reservations", route = RoutesBN.OFFICES),
        BottomNavItem(icon = Icons.Default.Search, label = "Search", route = "search"),
        BottomNavItem(icon = Icons.Default.Email, label = "Chats", route = "chats"),
        BottomNavItem(icon = Icons.Default.Person, label = "Profile", route = "profile")
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
                    if (item.route in listOf(RoutesBN.HOME, RoutesBN.OFFICES)) {
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
