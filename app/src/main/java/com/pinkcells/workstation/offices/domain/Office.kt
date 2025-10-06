package com.pinkcells.workstation.offices.domain

/**
 * Representa una oficina que puede ser alquilada.
 */
data class Office(
    val id: String? = null,
    val location: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val capacity: Int,
    val costPerDay: Int,
    val available: Boolean,
    val services: List<OfficeService> = emptyList(),
    val ratings: List<Rating> = emptyList()
)
