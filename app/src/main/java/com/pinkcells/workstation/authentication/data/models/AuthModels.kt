package com.pinkcells.workstation.authentication.data.models
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val passwordHash: String
)

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val dni: String,
    val phoneNumber: String,
    val email: String,
    val passwordHash: String,
    val role: Int
)

data class LoginResponse(
    val token: String?,
    val userId: Int?,
    val message: String?
)

data class SignUpResponse(
    val message: String?,
    val userId: String?,
    val success: Boolean = true
)

data class User(
    val id: String,
    val createdDate: String,
    val modifiedDate: String,
    val userId: Int,
    val updatedUserId: Int,
    val isActive: Boolean,
    val firstName: String,
    val lastName: String,
    val dni: String,
    val phoneNumber: String,
    val email: String,
    val role: Int,
    val createdAt: String,
    val passwordHash: String
)

data class ErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>? = null
)

data class PasswordResetRequest(
    val email: String
)