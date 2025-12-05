package com.pinkcells.workstation.chats.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

object FirestoreConfig {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance().apply {
            val setting = FirebaseFirestoreSettings.Builder()
                .build()
            firestoreSettings = setting
        }
    }
}