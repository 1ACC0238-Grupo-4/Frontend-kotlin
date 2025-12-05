package com.pinkcells.workstation.chats.data


import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

suspend fun signInAnonymously(): String {
    val res = Firebase.auth.signInAnonymously().await()
    return res.user?.uid ?: error("no uid")
}

fun currentUid(): String? = Firebase.auth.currentUser?.uid