package com.pinkcells.workstation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pinkcells.workstation.R
import com.pinkcells.workstation.navigation.Screen
import com.pinkcells.workstation.viewmodel.AuthViewModel

@Composable
fun LogInScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }

    var email by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var mostrarContraseña by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf("") }
    var errorContraseña by remember { mutableStateOf("") }
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

            Text(
                "Iniciar Sesión",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242)
            )

            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD4E5C3), shape = RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorEmail = ""; mensajeError = "" },
                    label = { Text("Correo electrónico") },
                    placeholder = { Text("Ejemplo: usuario@gmail.com") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (errorEmail.isNotEmpty()) {
                Text(
                    errorEmail,
                    fontSize = 11.sp,
                    color = Color(0xFFD32F2F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD4E5C3), shape = RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it; errorContraseña = ""; mensajeError = "" },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (mostrarContraseña) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { mostrarContraseña = !mostrarContraseña }) {
                            Icon(
                                imageVector = if (mostrarContraseña) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (mostrarContraseña) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (errorContraseña.isNotEmpty()) {
                Text(
                    errorContraseña,
                    fontSize = 11.sp,
                    color = Color(0xFFD32F2F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    mensajeError,
                    fontSize = 14.sp,
                    color = Color(0xFFD32F2F),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    errorEmail = if (email.isEmpty()) "El correo es requerido"
                    else if (!viewModel.validateEmail(email)) "Email inválido"
                    else ""
                    errorContraseña = if (contraseña.isEmpty()) "La contraseña es requerida" else ""

                    if (errorEmail.isEmpty() && errorContraseña.isEmpty()) {
                        viewModel.iniciarSesion(email, contraseña) { success, message ->
                            if (success) {
                                navController.navigate(Screen.HomePage.route) {
                                    popUpTo(Screen.IniciarSesion.route) { inclusive = true }
                                }
                            } else {
                                mensajeError = message
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F57D)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    "Iniciar Sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "¿Olvidaste tu contraseña?",
                fontSize = 14.sp,
                color = Color(0xFF212121),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.RecuperarContraseña.route)
                }
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "¿No tienes cuenta? ",
                    fontSize = 14.sp,
                    color = Color(0xFF424242)
                )
                Text(
                    "Regístrate aquí",
                    fontSize = 14.sp,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Registro1.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
