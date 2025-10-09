package com.pinkcells.workstation.login.domain 

interface AuthRepository {
    fun login(email: String, password: String): Boolean
    fun register(name: String, last: String, phone: String, email: String, password: String): Boolean
    fun resetPassword(email: String): Boolean
}