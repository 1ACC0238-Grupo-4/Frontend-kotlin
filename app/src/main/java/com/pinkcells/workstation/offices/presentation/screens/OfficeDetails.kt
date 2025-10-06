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
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinkcells.workstation.offices.presentation.viewmodel.OfficeDetailViewModel
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.pinkcells.workstation.offices.domain.Office
import com.pinkcells.workstation.offices.domain.OfficeService
import kotlinx.coroutines.coroutineScope
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.launch

@Composable
fun OfficeDetailPage(
    officeId: String? = null,
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val vm: OfficeDetailViewModel = viewModel()
    val officeState by vm.office.collectAsState()

    var ubicacion by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }
    var costoPorDia by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var disponible by remember { mutableStateOf(true) }

    var nuevoServicioNombre by remember { mutableStateOf("") }
    var nuevoServicioDescripcion by remember { mutableStateOf("") }
    var nuevoServicioCosto by remember { mutableStateOf("") }

    var servicios by remember { mutableStateOf(listOf<OfficeService>()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(officeId) {
        officeId?.let { vm.fetchOfficeById(it) }
    }

    LaunchedEffect(officeState) {
        officeState?.let { o ->
            ubicacion = o.location
            capacidad = o.capacity.toString()
            costoPorDia = o.costPerDay.toString()
            descripcion = o.description ?: ""
            imageUrl = o.imageUrl ?: ""
            disponible = o.available
            servicios = o.services ?: emptyList()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            HeaderSection()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFE8F36C),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Volver")
                    }

                    Text(
                        text = if (officeId == null) "Nueva Oficina" else "Editar Oficina",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                val pickImageLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) { uri ->
                    if (uri != null) imageUrl = uri.toString()
                }

                Button(
                    onClick = {
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9FD857))
                ) {
                    Text("Seleccionar imagen de galería", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(24.dp))

                OfficeTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    placeholder = "Ubicación"
                )

                Spacer(modifier = Modifier.height(12.dp))

                OfficeTextField(
                    value = capacidad,
                    onValueChange = { capacidad = it },
                    placeholder = "Capacidad (personas)"
                )

                Spacer(modifier = Modifier.height(12.dp))

                OfficeTextField(
                    value = costoPorDia,
                    onValueChange = { costoPorDia = it },
                    placeholder = "Costo por día (S/.)"
                )

                Spacer(modifier = Modifier.height(12.dp))

                OfficeTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = "Descripción"
                )

                Spacer(modifier = Modifier.height(12.dp))

                OfficeTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    placeholder = "URL de imagen"
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Disponible",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Switch(
                        checked = disponible,
                        onCheckedChange = { disponible = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF5BB318),
                            checkedTrackColor = Color(0xFF9FD857),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Servicios incluidos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                officeState?.services?.forEach { service ->
                    ServiceCard(
                        name = service.name,
                        description = service.description,
                        cost = service.cost
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                OfficeTextField(
                    value = nuevoServicioNombre,
                    onValueChange = { nuevoServicioNombre = it },
                    placeholder = "Nombre del servicio"
                )

                Spacer(modifier = Modifier.height(8.dp))

                OfficeTextField(
                    value = nuevoServicioDescripcion,
                    onValueChange = { nuevoServicioDescripcion = it },
                    placeholder = "Descripción del servicio"
                )

                Spacer(modifier = Modifier.height(8.dp))

                OfficeTextField(
                    value = nuevoServicioCosto,
                    onValueChange = { nuevoServicioCosto = it },
                    placeholder = "Costo del servicio (S/.)"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (nuevoServicioNombre.isNotBlank() && nuevoServicioCosto.toDoubleOrNull() != null) {
                            val nuevo = OfficeService(
                                name = nuevoServicioNombre,
                                description = nuevoServicioDescripcion,
                                cost = nuevoServicioCosto.toDouble().toInt()
                            )
                            servicios = servicios + nuevo

                            // Limpia los campos
                            nuevoServicioNombre = ""
                            nuevoServicioDescripcion = ""
                            nuevoServicioCosto = ""

                            // Mostrar snackbar
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Servicio agregado correctamente",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9FD857))
                ) {
                    Text("Agregar servicio", color = Color.Black)
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val canSave = ubicacion.isNotBlank() &&
                            capacidad.toIntOrNull() != null &&
                            costoPorDia.toIntOrNull() != null &&
                            descripcion.isNotBlank() &&
                            imageUrl.isNotBlank()

                    Button(
                        onClick = {
                            val office = Office(
                                id = officeId,
                                location = ubicacion,
                                description = descripcion,
                                imageUrl = imageUrl,
                                capacity = capacidad.toIntOrNull() ?: 0,
                                costPerDay = costoPorDia.toIntOrNull() ?: 0,
                                available = disponible,
                                services = servicios,
                                ratings = emptyList()
                            )
                            vm.saveOffice(office) { onSave() }
                        },
                        enabled = canSave,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (canSave) Color(0xFFE8F36C) else Color(0xFFBDBDBD)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Guardar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }

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

@Composable
fun ServiceCard(
    name: String,
    description: String,
    cost: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F36C)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Costo: S/. $cost",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun OfficeDetailPreview(){
    WorkstationTheme {
        OfficeDetailPage()
    }
}