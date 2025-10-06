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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinkcells.workstation.offices.presentation.components.OfficeCard
import com.pinkcells.workstation.offices.presentation.viewmodel.OfficesViewModel
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme


@Composable
fun OfficesPage(
    modifier: Modifier = Modifier,
    onOfficeClick: (String) -> Unit = {},
    onAddOffice: () -> Unit = {}
) {
    val vm: OfficesViewModel = viewModel()
    val oficinas by vm.offices.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchOffices()
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
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Offices",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
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
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(oficinas) { office ->
                        OfficeCard(
                            office = office,
                            onClick = { office.id?.let { onOfficeClick(it) } }
                        )
                    }
                }
            }
            Button(
                onClick = onAddOffice,
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
                    contentDescription = "Add",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Office",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
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
        OfficesPage(onOfficeClick = {}, onAddOffice = {})
    }
}
