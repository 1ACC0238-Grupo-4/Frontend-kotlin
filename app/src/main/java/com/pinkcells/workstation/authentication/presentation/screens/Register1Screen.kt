package com.pinkcells.workstation.authentication.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pinkcells.workstation.R
import com.pinkcells.workstation.navigation.Screen
import com.pinkcells.workstation.authentication.presentation.viewmodel.AuthViewModel

@Composable
fun Register1Screen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var repitaContraseña by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf("") }
    var errorApellido by remember { mutableStateOf("") }
    var errorDni by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var errorCelular by remember { mutableStateOf("") }
    var errorContraseña by remember { mutableStateOf("") }
    var errorRepitaContraseña by remember { mutableStateOf("") }

    var mostrarContraseña by remember { mutableStateOf(false) }
    var mostrarRepitaContraseña by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8BC34A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

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
                "Regístrate",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242)
            )

            Spacer(modifier = Modifier.height(32.dp))

            @Composable
            fun CampoTexto(
                value: String,
                onValueChange: (String) -> Unit,
                label: String,
                placeholder: String = "",
                isPassword: Boolean = false,
                mostrar: Boolean = false,
                onToggleMostrar: (() -> Unit)? = null
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFD4E5C3), shape = RoundedCornerShape(25.dp))
                        .padding(horizontal = 8.dp, vertical = 1.dp)
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        label = { Text(label) },
                        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (isPassword && !mostrar) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            if (isPassword && onToggleMostrar != null) {
                                IconButton(onClick = onToggleMostrar) {
                                    Icon(
                                        imageVector = if (mostrar) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (mostrar) "Ocultar contraseña" else "Mostrar contraseña"
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                }
            }

            CampoTexto(nombre, { nombre = it; errorNombre = "" }, "Nombre*")
            if (errorNombre.isNotEmpty()) Text(errorNombre, fontSize = 11.sp, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(apellido, { apellido = it; errorApellido = "" }, "Apellido*")
            if (errorApellido.isNotEmpty()) Text(errorApellido, fontSize = 11.sp, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(dni, {
                if (it.length <= 8 && it.all { char -> char.isDigit() }) dni = it
                errorDni = ""
            }, "DNI*")
            if (errorDni.isNotEmpty()) Text(errorDni, fontSize = 11.sp, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(email, { email = it; errorEmail = "" }, "Correo electrónico*", "Ejemplo: usuario@gmail.com")
            if (errorEmail.isNotEmpty()) Text(errorEmail, fontSize = 11.sp, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(celular, {
                if (it.length <= 9 && it.all { char -> char.isDigit() }) celular = it
                errorCelular = ""
            }, "Celular*")
            if (errorCelular.isNotEmpty()) Text(errorCelular, fontSize = 11.sp, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                contraseña,
                { contraseña = it; errorContraseña = "" },
                "Contraseña*",
                isPassword = true,
                mostrar = mostrarContraseña,
                onToggleMostrar = { mostrarContraseña = !mostrarContraseña }
            )
            if (errorContraseña.isNotEmpty()) Text(errorContraseña, fontSize = 11.sp, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                repitaContraseña,
                { repitaContraseña = it; errorRepitaContraseña = "" },
                "Repita Contraseña*",
                isPassword = true,
                mostrar = mostrarRepitaContraseña,
                onToggleMostrar = { mostrarRepitaContraseña = !mostrarRepitaContraseña }
            )
            if (errorRepitaContraseña.isNotEmpty()) Text(errorRepitaContraseña, fontSize = 11.sp, color = Color(0xFFD32F2F))

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        errorNombre = if (nombre.length < 3) "Nombre debe tener al menos 3 caracteres" else ""
                        errorApellido = if (apellido.length < 3) "Apellido debe tener al menos 3 caracteres" else ""
                        errorDni = if (dni.length != 8) "DNI debe tener 8 dígitos" else ""
                        errorEmail = if (!viewModel.validateEmail(email)) "Email inválido" else ""
                        errorCelular = if (celular.length != 9) "Celular debe tener 9 dígitos" else ""
                        errorContraseña = if (contraseña.length < 6) "Contraseña debe tener al menos 6 caracteres" else ""
                        errorRepitaContraseña = if (contraseña != repitaContraseña) "Las contraseñas no coinciden" else ""

                        if (
                            errorNombre.isEmpty() &&
                            errorApellido.isEmpty() &&
                            errorDni.isEmpty() &&
                            errorEmail.isEmpty() &&
                            errorCelular.isEmpty() &&
                            errorContraseña.isEmpty() &&
                            errorRepitaContraseña.isEmpty()
                        ) {
                            navController.currentBackStackEntry?.savedStateHandle?.apply {
                                set("nombre", nombre)
                                set("apellido", apellido)
                                set("dni", dni)
                                set("email", email)
                                set("celular", celular)
                                set("contraseña", contraseña)
                            }

                            navController.navigate(Screen.Registro2.route)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4E5C3)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("→", fontSize = 24.sp, color = Color(0xFF424242))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
