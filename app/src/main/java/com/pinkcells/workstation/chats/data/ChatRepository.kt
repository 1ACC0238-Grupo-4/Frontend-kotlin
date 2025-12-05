package com.pinkcells.workstation.chats.data


import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.pinkcells.workstation.chats.domain.Message
import com.pinkcells.workstation.chats.domain.conversationIdOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID


class ChatRepository(private val db: com.google.firebase.firestore.FirebaseFirestore = FirestoreConfig.db) {

    /** Tiempo real: mensajes de una conversación */
    fun subscribeToConversation(
        conversationId: String,
        onUpdate: (List<Pair<Message, Boolean>>) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration {
        return db.collection("messages")
            .whereEqualTo("conversationId", conversationId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snaps, e ->
                if (e != null) {
                    Log.e("ChatRepository", "subscribeToConversation error", e)
                    onError(e)
                    return@addSnapshotListener
                }
                val list = snaps?.documents?.mapNotNull { doc ->
                    val msg = doc.toObject(Message::class.java)
                    msg?.let { it to doc.metadata.hasPendingWrites() }
                } ?: emptyList()
                onUpdate(list)
            }
    }

    suspend fun sendMessageWithId(
        id: String,
        senderId: String,
        receiverId: String,
        content: String
    ): Result<Message> {
        return try {
            val convId = conversationIdOf(senderId, receiverId)
            val now = System.currentTimeMillis()
            val authorUid = com.google.firebase.Firebase.auth.currentUser?.uid ?: ""

            val msg = Message(
                id = id, // 👈 usar el id recibido
                conversationId = convId,
                senderId = senderId,
                receiverId = receiverId,
                content = content,
                timestamp = now,
                authorUid = authorUid,
                status = "SENT"
            )

            db.collection("messages").document(id).set(msg).await()
            Result.success(msg)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    /** Enviar mensaje (Firestore encola offline y reintenta; reglas validan authorUid) */
    suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        content: String
    ): Result<Message> = withContext(Dispatchers.IO) {
        try {
            val convId = conversationIdOf(senderId, receiverId)
            val id = UUID.randomUUID().toString()
            val now = System.currentTimeMillis()
            val authorUid = Firebase.auth.currentUser?.uid ?: ""

            val msg = Message(
                id = id,
                conversationId = convId,
                senderId = senderId,
                receiverId = receiverId,
                content = content,
                timestamp = now,
                authorUid = authorUid,
                status = "SENT"
            )

            db.collection("messages").document(id).set(msg).await()
            Result.success(msg)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
