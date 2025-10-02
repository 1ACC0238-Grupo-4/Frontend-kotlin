package com.pinkcells.workstation.offices.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinkcells.workstation.offices.presentation.components.OfficeCard
import com.pinkcells.workstation.offices.presentation.components.Office
import com.pinkcells.workstation.shared.presentation.components.BottomNavigationBar
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme


@Composable
fun OficinasPage() {
    var selectedTab by remember { mutableIntStateOf(1) }

    // Estado para las oficinas (en producción usarías ViewModel + StateFlow)
    var oficinas by remember {
        mutableStateOf(
            listOf(
                Office(
                    id = 1,
                    imageUrl = "https://images.unsplash.com/photo-1497366216548-37526070297c",
                    ubicacion = "Piso 1 - Sala A",
                    capacidad = 8,
                    descripcion = "Oficina ejecutiva con vista panorámica y equipamiento completo"
                ),
                Office(
                    id = 2,
                    imageUrl = "https://images.unsplash.com/photo-1497366811353-6870744d04b2",
                    ubicacion = "Piso 2 - Sala B",
                    capacidad = 12,
                    descripcion = "Sala de conferencias con proyector y sistema de videoconferencia"
                ),
                Office(
                    id = 3,
                    imageUrl = "https://images.unsplash.com/photo-1497366754035-f200968a6e72",
                    ubicacion = "Piso 3 - Sala C",
                    capacidad = 20,
                    descripcion = "Auditorio espacioso ideal para presentaciones y eventos corporativos"
                ),
                Office(
                    id = 4,
                    imageUrl = "https://images.unsplash.com/photo-1542744173-8e7e53415bb0",
                    ubicacion = "Piso 1 - Sala D",
                    capacidad = 6,
                    descripcion = "Sala de reuniones privada con pizarra inteligente y conexión WiFi"
                )
            )
        )
    }

    // Estado de carga (para cuando llames a tu API)
    var isLoading by remember { mutableStateOf(false) }

    // Aquí llamarías a tu API
    LaunchedEffect(Unit) {
        // Ejemplo de cómo cargar desde API:
        // isLoading = true
        // try {
        //     oficinas = apiService.getOficinas()
        // } catch (e: Exception) {
        //     // Manejar error
        // } finally {
        //     isLoading = false
        // }
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

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = "Oficinas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Mostrar loading o lista
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF5BB318))
                    }
                } else {
                    // Lista de oficinas
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(oficinas) { office ->
                            OfficeCard(
                                office = office,
                                onClick = {
                                    println("Click en oficina: ${office.ubicacion}")
                                }
                            )
                        }
                    }
                }

                // Botón de agregar
                Button(
                    onClick = { /* Acción para agregar oficina */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9FD857)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Agregar Oficina",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
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
fun OfficePagePreview(){
    WorkstationTheme {
        OficinasPage()
    }
}