package com.pinkcells.workstation.chats.domain

data class Message (
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val authorUid: String = "",
    val status: String = "SENT"
)