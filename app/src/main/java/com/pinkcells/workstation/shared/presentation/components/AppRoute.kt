package com.pinkcells.workstation.shared.presentation.components

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pinkcells.workstation.authentication.presentation.screens.PerfilScreen
import com.pinkcells.workstation.authentication.presentation.viewmodel.AuthViewModel
import com.pinkcells.workstation.chats.data.ChatRepository
import com.pinkcells.workstation.chats.data.signInAnonymously
import com.pinkcells.workstation.chats.presentation.screens.ChatScreen
import com.pinkcells.workstation.chats.presentation.screens.ChatsPage
import com.pinkcells.workstation.chats.presentation.viewmodel.ChatViewModel
import com.pinkcells.workstation.offices.presentation.screens.OfficeDetailPage
import com.pinkcells.workstation.offices.presentation.screens.OfficesPage
import com.pinkcells.workstation.offices.presentation.screens.SearchOfficesPage
import com.pinkcells.workstation.shared.presentation.screens.HomePage
import kotlinx.coroutines.launch

object RoutesBN {
    const val HOME = "home"
    const val OFFICES = "offices"
    const val OFFICE_DETAIL = "office/{officeId}"
    const val OFFICE_CREATE = "office/new"

    //[1]Crear nueva ruta dentro de RoutesBN
    const val SEARCH_OFFICE = "search_office"

//    const val SEARCH = "search"
    const val CHATS = "chats"

    const val CHATSCREEN = "chat_screen/{peerId}/{peerName}"

    const val PROFILE = "profile"
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        try {
            signInAnonymously()
        } catch (_: Exception) {
            // TODO: log si quieres mostrar algo al usuario; para MVP puedes ignorar
        }
    }

    val context = LocalContext.current
    val authVM = remember { AuthViewModel(context) }


    val myUserId = remember { authVM.getUserId().toString() }

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
            //[2]Agregar la pantalla al Navhost
            composable(RoutesBN.SEARCH_OFFICE) {
                SearchOfficesPage()
            }

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

            composable(RoutesBN.CHATS) {
                ChatsPage(
                    onChatClick = { peerId, peerName ->
                        val encodedName = Uri.encode(peerName)
                        navController.navigate("chat_screen/$peerId/$encodedName") {
                            launchSingleTop = true
                        }
                    }
                )
            }


            composable(
                route = RoutesBN.CHATSCREEN,
                arguments = listOf(navArgument("peerId"){type= NavType.StringType})
            ){ backStackEntry ->
                val peerId = backStackEntry.arguments?.getString("peerId")?: ""
                val peerName = backStackEntry.arguments?.getString("peerName")?: ""

                val repo = ChatRepository()
                val chatVm = ChatViewModel(
                    repo = repo,
                    myUserId = myUserId,
                    peerUserId = peerId
                )
                ChatScreen(vm= chatVm, title = "Chat con $peerName")
            }

            composable(route = RoutesBN.PROFILE){
                PerfilScreen(navController = navController)
            }
        }
    }
}
