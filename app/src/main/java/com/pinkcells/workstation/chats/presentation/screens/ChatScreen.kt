
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
    vm: ChatViewModel,
    modifier: Modifier = Modifier,
    title: String = "Chat"
) {
    val uiMessages by vm.messages.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ChatHeader(title = title)

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
private fun ChatHeader(
    title: String,
) {
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

        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 12.dp)
        )
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

/* ----------------------------------------------------------------------------------------------
   PREVIEW
   El Preview NO usa Firestore ni tu ChatViewModel real. Inyectamos mensajes mock para ver el UI.
   ---------------------------------------------------------------------------------------------- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    WorkstationTheme {
        // Mensajes ficticios para ver el diseño:
        val previewMessages = listOf(
            UiMessage(
                msg = com.pinkcells.workstation.chats.domain.Message(
                    id = "m1",
                    conversationId = "userA_userB",
                    senderId = "userA",           // soy yo
                    receiverId = "userB",
                    content = "Hola, ¿cómo vas?",
                    timestamp = System.currentTimeMillis() - 60_000,
                    authorUid = "preview-uid",
                    status = "SENT"
                ),
                pending = false
            ),
            UiMessage(
                msg = com.pinkcells.workstation.chats.domain.Message(
                    id = "m2",
                    conversationId = "userA_userB",
                    senderId = "userB",           // el otro
                    receiverId = "userA",
                    content = "Bien, gracias. ¿Y tú?",
                    timestamp = System.currentTimeMillis() - 30_000,
                    authorUid = "preview-uid",
                    status = "SENT"
                ),
                pending = false
            ),
            UiMessage(
                msg = com.pinkcells.workstation.chats.domain.Message(
                    id = "m3",
                    conversationId = "userA_userB",
                    senderId = "userA",
                    receiverId = "userB",
                    content = "Probando envío pendiente...",
                    timestamp = System.currentTimeMillis() - 10_000,
                    authorUid = "preview-uid",
                    status = "SENT"
                ),
                pending = true // ⏳ simula mensaje en cola/offline
            )
        )

        // Renderizamos la UI base con los mock, sin usar VM (para evitar dependencias en preview)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            ChatHeader(title = "Chat (Preview)")
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp)
            ) {
                items(previewMessages) { item ->
                    val mine = item.msg.senderId == "userA"
                    MessageBubble(text = item.msg.content, isMine = mine, pending = item.pending)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            ChatInput(onSend = { /* no-op en preview */ })
        }
    }
}
