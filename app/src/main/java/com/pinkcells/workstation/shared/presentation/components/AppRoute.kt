package com.pinkcells.workstation.shared.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pinkcells.workstation.authentication.presentation.screens.PerfilScreen
import com.pinkcells.workstation.offices.presentation.screens.OfficeDetailPage
import com.pinkcells.workstation.offices.presentation.screens.OfficesPage
import com.pinkcells.workstation.shared.presentation.screens.HomePage

object RoutesBN {
    const val HOME = "home"
    const val OFFICES = "offices"
    const val OFFICE_DETAIL = "office/{officeId}"
    const val OFFICE_CREATE = "office/new"
//    const val SEARCH = "search"
//    const val CHATS = "chats"

    const val PROFILE = "profile"
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RoutesBN.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(RoutesBN.HOME) { HomePage() }

            composable(RoutesBN.OFFICES) {
                OfficesPage(
                    onOfficeClick = { id ->
                        navController.navigate("office/$id") {
                            launchSingleTop = true
                        }
                    },
                    onAddOffice = {
                        navController.navigate(RoutesBN.OFFICE_CREATE) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Edit existing office
            composable(
                route = RoutesBN.OFFICE_DETAIL,
                arguments = listOf(navArgument("officeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val officeId = backStackEntry.arguments?.getString("officeId")
                OfficeDetailPage(
                    officeId = officeId,
                    onBack = { navController.popBackStack() },
                    onSave = {
                        navController.popBackStack()
                    }
                )
            }

            // Create new office
            composable(RoutesBN.OFFICE_CREATE) {
                OfficeDetailPage(
                    officeId = null,
                    onBack = { navController.popBackStack() },
                    onSave = {
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(route = RoutesBN.PROFILE){
                PerfilScreen(navController = navController)
            }
        }
    }
}
