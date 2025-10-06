package com.pinkcells.workstation.offices.domain

/**
 * Representa un servicio adicional que ofrece una oficina (ej. Wi-Fi, Proyector).
 */
data class OfficeService(
    val name: String,
    val description: String,
    val cost: Int
)


