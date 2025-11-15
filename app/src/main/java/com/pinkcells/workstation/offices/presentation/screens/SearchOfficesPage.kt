package com.pinkcells.workstation.offices.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pinkcells.workstation.offices.data.OfficesRepository
import com.pinkcells.workstation.offices.domain.Office
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme
import kotlinx.coroutines.launch
import android.util.Log

@Composable
fun SearchOfficesPage(
    modifier: Modifier = Modifier,
    onOfficeClick: (String) -> Unit = {}
) {
    val offices by OfficesRepository.officesFlow.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCapacityFilter by remember { mutableStateOf<Int?>(null) }
    var sortByCapacity by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Filtrar oficinas según búsqueda y filtros
    val filteredOffices = remember(offices, searchQuery, selectedCapacityFilter, sortByCapacity) {
        var result = offices

        // Filtro de búsqueda por texto
        if (searchQuery.isNotBlank()) {
            result = result.filter { office ->
                office.location?.contains(searchQuery, ignoreCase = true) == true ||
                        office.description?.contains(searchQuery, ignoreCase = true) == true
            }
        }

        // Filtro por capacidad mínima
        if (selectedCapacityFilter != null) {
            result = result.filter { office ->
                (office.capacity ?: 0) >= selectedCapacityFilter!!
            }
        }

        // Ordenar por capacidad
        if (sortByCapacity) {
            result = result.sortedByDescending { it.capacity ?: 0 }
        }

        result
    }

    LaunchedEffect(Unit) {
        if (offices.isEmpty()) {
            isLoading = true
            scope.launch {
                OfficesRepository.fetchOffices()
                isLoading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeaderSection()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Busca tu oficina",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SearchBarWithFilter(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                capacityFilter = selectedCapacityFilter,
                sortByCapacity = sortByCapacity,
                onCapacityFilterChange = { selectedCapacityFilter = it },
                onSortByCapacityChange = { sortByCapacity = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Oficinas recomendadas",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color(0xFF5BB318))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Cargando oficinas...", fontSize = 12.sp)
                        }
                    }
                }
                filteredOffices.isEmpty() && searchQuery.isNotBlank() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No se encontraron oficinas",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Intenta con otra búsqueda",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                offices.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "No hay oficinas disponibles",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        isLoading = true
                                        OfficesRepository.fetchOffices()
                                        isLoading = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5BB318)
                                )
                            ) {
                                Text("Reintentar", color = Color.White)
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredOffices) { office ->
                            SearchOfficeCard(
                                office = office,
                                onClick = {
                                    Log.d("SearchOfficesPage", "Navegando a oficina: ${office.id}")
                                    office.id?.let { onOfficeClick(it) }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SearchBarWithFilter(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    capacityFilter: Int?,
    sortByCapacity: Boolean,
    onCapacityFilterChange: (Int?) -> Unit,
    onSortByCapacityChange: (Boolean) -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Barra de búsqueda FUNCIONAL
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(25.dp),
            color = Color(0xFFE8F36C),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.Black),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Buscar oficina...",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        // Botón de filtro con indicador
        Surface(
            modifier = Modifier.size(50.dp),
            shape = RoundedCornerShape(25.dp),
            color = if (capacityFilter != null || sortByCapacity)
                Color(0xFF9FD857) else Color(0xFFE8F36C),
            shadowElevation = 2.dp,
            onClick = { showFilterDialog = true }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Filter",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )

                // Indicador de filtros activos
                if (capacityFilter != null || sortByCapacity) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(8.dp)
                            .background(Color.Red, shape = androidx.compose.foundation.shape.CircleShape)
                    )
                }
            }
        }
    }

    // Diálogo de filtros FUNCIONAL
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Filtros de búsqueda",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Filtro por capacidad
                    Text(
                        "Capacidad mínima",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(null, 5, 10, 20, 50).forEach { capacity ->
                            FilterChip(
                                selected = capacityFilter == capacity,
                                onClick = { onCapacityFilterChange(capacity) },
                                label = {
                                    Text(
                                        if (capacity == null) "Todas" else "$capacity+",
                                        fontSize = 14.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF9FD857),
                                    selectedLabelColor = Color.Black
                                )
                            )
                        }
                    }

                    Divider()

                    // Ordenar por capacidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Ordenar por capacidad",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Switch(
                            checked = sortByCapacity,
                            onCheckedChange = onSortByCapacityChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF5BB318)
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFilterDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5BB318)
                    )
                ) {
                    Text("Aplicar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCapacityFilterChange(null)
                        onSortByCapacityChange(false)
                    }
                ) {
                    Text("Limpiar filtros", color = Color(0xFF5BB318))
                }
            }
        )
    }
}

@Composable
private fun SearchOfficeCard(
    office: Office,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F36C)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Imagen con fallback mejorado
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFBDBDBD)),
                contentAlignment = Alignment.Center
            ) {
                if (!office.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = office.imageUrl,
                        contentDescription = "Office ${office.id}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onError = { error ->
                            Log.e("SearchOfficesPage", "Error cargando imagen: ${office.imageUrl}")
                        },
                        onSuccess = {
                            Log.d("SearchOfficesPage", "Imagen cargada: ${office.imageUrl}")
                        }
                    )
                } else {
                    // Placeholder visual cuando no hay imagen
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Sin imagen",
                            fontSize = 9.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información de la oficina
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Título y ubicación
                Column {
                    Text(
                        text = office.location ?: "Oficina",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1
                    )

                    if (!office.description.isNullOrBlank()) {
                        Text(
                            text = office.description,
                            fontSize = 11.sp,
                            color = Color.DarkGray,
                            maxLines = 2
                        )
                    }
                }

                // Botón "Ver más"
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9FD857)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Ver más",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
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
fun SearchOfficesPagePreview() {
    WorkstationTheme {
        SearchOfficesPage()
    }
}