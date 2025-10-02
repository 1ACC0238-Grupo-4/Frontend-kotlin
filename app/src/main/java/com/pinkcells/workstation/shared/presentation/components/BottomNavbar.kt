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

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color(0xFF5BB318),
        modifier = Modifier.height(80.dp)
    ) {
        val items = listOf(
            BottomNavItem(Icons.Default.Home, "Inicio", 0),
            BottomNavItem(Icons.Default.DateRange, "Reservas", 1),
            BottomNavItem(Icons.Default.Search, "Buscar", 2),
            BottomNavItem(Icons.Default.Email, "Chats", 3),
            BottomNavItem(Icons.Default.Person, "Perfil", 4)
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                if (selectedTab == item.index) Color( 0xFFE8F36C)
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
                selected = selectedTab == item.index,
                onClick = { onTabSelected(item.index) },
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
    val index: Int
)