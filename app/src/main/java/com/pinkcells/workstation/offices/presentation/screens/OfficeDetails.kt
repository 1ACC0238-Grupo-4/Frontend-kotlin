package com.pinkcells.workstation.offices.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pinkcells.workstation.shared.presentation.components.BottomNavigationBar
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme

@Composable
fun OfficeDetailPage(
    officeId: Int? = null, // null significa que es nueva oficina
    onSave: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
    onCancel: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(1) }

    // Estados para los campos de texto
    var nombreOficina by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Si es edición, cargarías los datos aquí
    LaunchedEffect(officeId) {
        if (officeId != null) {
            // Aquí cargarías los datos de la oficina desde tu API/ViewModel
            nombreOficina = "Oficina $officeId"
            // ubicacion = ...
            // etc.
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Header con curva verde
            HeaderSection()

            // Contenido principal con scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = if (officeId == null) "Oficina 1" else "Editar Oficina",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Imagen de la oficina
                AsyncImage(
                    model = imageUrl.ifEmpty { "https://images.unsplash.com/photo-1497366216548-37526070297c" },
                    contentDescription = "Imagen de oficina",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo: Nombre de oficina
                OfficeTextField(
                    value = nombreOficina,
                    onValueChange = { nombreOficina = it },
                    placeholder = "Nombre de la oficina"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Ubicación
                OfficeTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    placeholder = "Ubicación"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Capacidad
                OfficeTextField(
                    value = capacidad,
                    onValueChange = { capacidad = it },
                    placeholder = "Capacidad"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Descripción
                OfficeTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = "Descripción"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: URL de imagen
                OfficeTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    placeholder = "URL de imagen"
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Guardar cambios
                    Button(
                        onClick = {
                            onSave(nombreOficina, ubicacion, capacidad, descripcion, imageUrl)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE8F36C)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Guardar cambios",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }

                    // Botón Cancelar
                    Button(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE8F36C)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun OfficeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE8F36C),
            unfocusedContainerColor = Color(0xFFE8F36C),
            disabledContainerColor = Color(0xFFE8F36C),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawRoundRect(
                color = Color(0xFF5BB318),
                size = size.copy(height = size.height * 0.6f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(0f, 0f)
            )

            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, size.height * 0.4f)
                quadraticBezierTo(
                    size.width / 2f, size.height * 0.9f,
                    size.width, size.height * 0.4f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(
                path = path,
                color = Color(0xFF5BB318)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OfficeDetailPreview(){
    WorkstationTheme {
        OfficeDetailPage{}
    }
}