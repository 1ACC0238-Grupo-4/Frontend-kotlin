package com.pinkcells.workstation.chats.domain

fun conversationIdOf(userA: String, userB: String): String {
    return if (userA <= userB) "${userA}_$userB" else "${userB}_$userA"
}