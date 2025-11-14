package com.pinkcells.workstation.authentication.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
fun RecoverPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }

    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }
    var paso by remember { mutableStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8BC34A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(Color(0xFFF4F57D), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo Workstation",
                    modifier = Modifier.size(75.dp)
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
                "Recuperar Contraseña",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242)
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (paso == 1) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Ingresa tu correo electrónico y enviaremos instrucciones para recuperar tu contraseña.",
                        fontSize = 15.sp,
                        color = Color(0xFF424242),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFD4E5C3), shape = RoundedCornerShape(18.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorEmail = "" },
                            label = { Text("Correo electrónico") },
                            placeholder = { Text("Ejemplo: usuario@gmail.com") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }

                    if (errorEmail.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            errorEmail,
                            fontSize = 12.sp,
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {
                            if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
                                errorEmail = "Por favor ingresa un correo válido"
                            } else {
                                viewModel.recuperarContraseña(email) { success, message ->
                                    if (success) {
                                        mensajeExito = message
                                        paso = 2
                                    } else {
                                        errorEmail = message
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F57D)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            "Enviar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("✓", fontSize = 80.sp, color = Color(0xFF66BB6A))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        mensajeExito,
                        fontSize = 18.sp,
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Revisa tu bandeja de entrada y sigue las instrucciones.",
                        fontSize = 15.sp,
                        color = Color(0xFF424242),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = {
                            navController.navigate(Screen.IniciarSesion.route) {
                                popUpTo(Screen.IniciarSesion.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F57D)),
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        Text(
                            "Volver al inicio de sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (paso == 1) {
                Spacer(modifier = Modifier.height(24.dp))
                FloatingActionButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom = 40.dp),
                    containerColor = Color(0xFFF4F57D),
                    contentColor = Color(0xFF212121),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
