package com.pinkcells.workstation.offices.domain

import java.util.Date

/**
 * Representa una calificación dada por un usuario a una oficina.
 */
data class Rating(
    val id: String? = null,
    val score: Int,
    val comment: String? = null,
    val officeId: String,
    val createdAt: Date? = null
)

