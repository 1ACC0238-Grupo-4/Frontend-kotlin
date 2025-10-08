package com.pinkcells.workstation.users.domain

data class User(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val dni: String,
    val phoneNumber: String,
    val email: String,
    val role: Int,
    val password: String,
)