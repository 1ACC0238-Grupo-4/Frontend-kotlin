
package com.pinkcells.workstation.chats.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinkcells.workstation.chats.presentation.viewmodel.ChatViewModel
import com.pinkcells.workstation.chats.presentation.viewmodel.UiMessage
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme

@Composable
fun ChatScreen(
    title: String? = null,
    vm: ChatViewModel,
    modifier: Modifier = Modifier,
) {
    val uiMessages by vm.messages.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeaderSection()

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "$title",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        // Lista de mensajes
        if (uiMessages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no hay mensajes",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp)
            ) {
                items(uiMessages) { item: UiMessage ->
                    val mine = item.msg.senderId == vm.myUserId
                    MessageBubble(
                        text = item.msg.content,
                        isMine = mine,
                        pending = item.pending
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Input + enviar
        ChatInput(
            onSend = { text ->
                if (text.isNotBlank()) vm.send(text)
            }
        )
    }
}

/** Encabezado visual simple, consistente con tu estilo de ChatsPage */
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
                quadraticTo(
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

/** Burbuja de mensaje con alineación (derecha si es mío), y tag de pendiente ⏳ */
@Composable
private fun MessageBubble(
    text: String,
    isMine: Boolean,
    pending: Boolean
) {
    val bg = if (isMine) Color(0xFFE8F5E9) else Color(0xFFF5F5F5) // verde muy claro vs gris claro
    val align = if (isMine) Arrangement.End else Arrangement.Start
    val pendingTag = if (pending) "" else ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = align
    ) {
        Box(
            modifier = Modifier
                .background(bg)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(text = text + pendingTag, color = Color.Black)
        }
    }
}

/** Input de chat con TextField y botón enviar */
@Composable
private fun ChatInput(
    onSend: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (input.isNotBlank()) {
                onSend(input.trim())
                input = ""
            }
        }) {
            Text("Enviar")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    WorkstationTheme {
    }
}
