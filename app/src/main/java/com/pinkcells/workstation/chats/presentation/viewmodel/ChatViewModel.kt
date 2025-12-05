package com.pinkcells.workstation.chats.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.pinkcells.workstation.chats.data.ChatRepository
import com.pinkcells.workstation.chats.domain.Message
import com.pinkcells.workstation.chats.domain.conversationIdOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class UiMessage(val msg: Message, val pending: Boolean)

class ChatViewModel(private val repo: ChatRepository, val myUserId: String, val peerUserId: String) : ViewModel() {

    private val _messages = MutableStateFlow<List<UiMessage>>(emptyList())
    val messages: StateFlow<List<UiMessage>> = _messages

    private var registration: ListenerRegistration? = null

    init { subscribe()
    }

    private fun subscribe() {
        val convId = conversationIdOf(myUserId, peerUserId)
        registration = repo.subscribeToConversation(
            convId,
            onUpdate = { pairs -> _messages.value = pairs.map { (m, p) -> UiMessage(m, p) } },
            onError = { /* TODO: mostrar error */ }
        )
    }

    fun send(text: String) {
        val stableId = "msg-${System.currentTimeMillis()}-${myUserId}"
        val convId = conversationIdOf(myUserId, peerUserId)
        val localMsg = Message(
            id = stableId,
            conversationId = convId,
            senderId = myUserId,
            receiverId = peerUserId,
            content = text,
            timestamp = System.currentTimeMillis(),
            authorUid = "",
            status = "SENT"
        )
        _messages.value = _messages.value + UiMessage(localMsg, pending = true)


        viewModelScope.launch {
            repo.sendMessageWithId(
                id = stableId, // 👈 pasas el mismo id al repo
                senderId = myUserId,
                receiverId = peerUserId,
                content = text
            ).onFailure {
                // manejar error
            }
        }

    }

    override fun onCleared() {
        registration?.remove()
        super.onCleared()
    }

}