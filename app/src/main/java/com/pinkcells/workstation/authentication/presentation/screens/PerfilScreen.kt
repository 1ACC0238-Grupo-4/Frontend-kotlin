package com.pinkcells.workstation.authentication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pinkcells.workstation.authentication.presentation.viewmodel.AuthViewModel

@Composable
fun PerfilScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }

    var modoEdicion by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf("") }
    var errorApellido by remember { mutableStateOf("") }
    var errorCelular by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.obtenerDatosUsuario { usuario ->
            nombre = usuario.nombre
            apellido = usuario.apellido
            dni = usuario.dni
            email = usuario.email
            celular = usuario.celular
            tipoUsuario = usuario.tipoUsuario
        }
    }

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
                    .size(100.dp)
                    .background(Color(0xFF66BB6A), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${nombre.firstOrNull()?.uppercase()}${apellido.firstOrNull()?.uppercase()}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "$nombre $apellido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Text(
                tipoUsuario,
                fontSize = 16.sp,
                color = Color(0xFF424242)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; errorNombre = "" },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = modoEdicion,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD4E5C3),
                    unfocusedContainerColor = Color(0xFFD4E5C3),
                    disabledContainerColor = Color(0xFFD4E5C3),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp)
            )
            if (errorNombre.isNotEmpty()) {
                Text(errorNombre, fontSize = 11.sp, color = Color(0xFFD32F2F), modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it; errorApellido = "" },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = modoEdicion,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD4E5C3),
                    unfocusedContainerColor = Color(0xFFD4E5C3),
                    disabledContainerColor = Color(0xFFD4E5C3),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp)
            )
            if (errorApellido.isNotEmpty()) {
                Text(errorApellido, fontSize = 11.sp, color = Color(0xFFD32F2F), modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dni,
                onValueChange = {},
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color(0xFFE0E0E0),
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = Color(0xFF757575)
                ),
                shape = RoundedCornerShape(25.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color(0xFFE0E0E0),
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = Color(0xFF757575)
                ),
                shape = RoundedCornerShape(25.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = celular,
                onValueChange = {
                    if (it.length <= 9 && it.all { char -> char.isDigit() }) {
                        celular = it
                        errorCelular = ""
                    }
                },
                label = { Text("Celular") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = modoEdicion,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD4E5C3),
                    unfocusedContainerColor = Color(0xFFD4E5C3),
                    disabledContainerColor = Color(0xFFD4E5C3),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(25.dp)
            )
            if (errorCelular.isNotEmpty()) {
                Text(errorCelular, fontSize = 11.sp, color = Color(0xFFD32F2F), modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }

            if (mensajeExito.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    mensajeExito,
                    fontSize = 14.sp,
                    color = Color(0xFF66BB6A),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (modoEdicion) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            modoEdicion = false
                            viewModel.obtenerDatosUsuario { usuario ->
                                nombre = usuario.nombre
                                apellido = usuario.apellido
                                celular = usuario.celular
                            }
                            mensajeExito = ""
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Cancelar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = {
                            errorNombre = if (nombre.length < 3) "Nombre debe tener al menos 3 caracteres" else ""
                            errorApellido = if (apellido.length < 3) "Apellido debe tener al menos 3 caracteres" else ""
                            errorCelular = if (celular.length != 9) "Celular debe tener 9 dígitos" else ""

                            if (errorNombre.isEmpty() && errorApellido.isEmpty() && errorCelular.isEmpty()) {
                                viewModel.actualizarDatosUsuario(
                                    nombre = nombre,
                                    apellido = apellido,
                                    celular = celular
                                ) { success, message ->
                                    if (success) {
                                        mensajeExito = message
                                        modoEdicion = false
                                    } else {
                                        mensajeExito = message
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Guardar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            } else {
                Button(
                    onClick = {
                        modoEdicion = true
                        mensajeExito = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F57D)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        "Editar Perfil",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.cerrarSesion()
                        navController.navigate("iniciar_sesion") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        "Cerrar Sesión",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}