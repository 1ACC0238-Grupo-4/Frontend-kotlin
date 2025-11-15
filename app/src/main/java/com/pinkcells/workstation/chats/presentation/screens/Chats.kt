package com.pinkcells.workstation.chats.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinkcells.workstation.chats.presentation.components.ChatCard
import com.pinkcells.workstation.chats.presentation.viewmodel.ChatsViewModel
import com.pinkcells.workstation.offices.presentation.components.OfficeCard
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme


@Composable
fun ChatsPage(
    modifier: Modifier = Modifier,
    onChatClick: (String) -> Unit = {},
) {
    val vm: ChatsViewModel = viewModel()
    val chats by vm.chats.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        HeaderSection()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal =  20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Chats",
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
            }else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(chats) { chat ->
                        ChatCard(
                            chat = chat,
                            onClick = { chat.id?.let { onChatClick(it) } }
                        )
                    }
                    item{
                        if (chats.isEmpty()){
                            Text(
                                text = "no tiene chats disponibles",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
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
fun ChatsPagePreview(){
    WorkstationTheme {
        ChatsPage(onChatClick = {} )
    }
}