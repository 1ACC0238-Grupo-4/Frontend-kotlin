package com.pinkcells.workstation.authentication.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pinkcells.workstation.R
import com.pinkcells.workstation.navigation.Screen
import com.pinkcells.workstation.authentication.presentation.viewmodel.AuthViewModel

@Composable
fun Register2Screen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }

    val nombre = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nombre") ?: ""
    val apellido = navController.previousBackStackEntry?.savedStateHandle?.get<String>("apellido") ?: ""
    val dni = navController.previousBackStackEntry?.savedStateHandle?.get<String>("dni") ?: ""
    val email = navController.previousBackStackEntry?.savedStateHandle?.get<String>("email") ?: ""
    val celular = navController.previousBackStackEntry?.savedStateHandle?.get<String>("celular") ?: ""
    val contraseña = navController.previousBackStackEntry?.savedStateHandle?.get<String>("contraseña") ?: ""

    var tipoUsuarioSeleccionado by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8BC34A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFFF4F57D), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo Workstation",
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Workstation",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF4F57D), shape = RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "¿Ofreces oficinas para\nalquilar?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            tipoUsuarioSeleccionado = "Arrendador"
                            mensajeError = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tipoUsuarioSeleccionado == "Arrendador") Color(0xFF66BB6A) else Color(0xFFD4E5C3)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            "Sí",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            tipoUsuarioSeleccionado = "Arrendatario"
                            mensajeError = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tipoUsuarioSeleccionado == "Arrendatario") Color(0xFF66BB6A) else Color(0xFFD4E5C3)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            "No",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                    }
                }
            }

            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    mensajeError,
                    fontSize = 14.sp,
                    color = Color(0xFFD32F2F),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4E5C3)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("←", fontSize = 24.sp, color = Color(0xFF424242))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (tipoUsuarioSeleccionado.isEmpty()) {
                            mensajeError = "Por favor, selecciona una opción"
                        } else {
                            viewModel.registrarUsuario(
                                nombre = nombre,
                                apellido = apellido,
                                dni = dni,
                                email = email,
                                celular = celular,
                                contraseña = contraseña,
                                tipoUsuario = tipoUsuarioSeleccionado
                            ) { success, message ->
                                if (success) {
                                    if (tipoUsuarioSeleccionado == "Arrendador") {
                                        navController.navigate(Screen.Registro1.route) {
                                            popUpTo(Screen.IniciarSesion.route) { inclusive = false }
                                        }
                                    } else {
                                        navController.navigate(Screen.HomePage.route) {
                                            popUpTo(Screen.IniciarSesion.route) { inclusive = true }
                                        }
                                    }
                                } else {
                                    mensajeError = message
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4E5C3)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("✓", fontSize = 24.sp, color = Color(0xFF424242))
                }
            }
        }
    }
}