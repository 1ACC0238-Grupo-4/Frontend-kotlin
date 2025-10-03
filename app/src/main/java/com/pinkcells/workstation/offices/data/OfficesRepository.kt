package com.pinkcells.workstation.offices.data

import com.pinkcells.workstation.offices.presentation.components.Office

object OfficesRepository {
    // In-memory sample data. Replace with Retrofit/API later.
    private val initial = listOf(
        Office(
            id = 1,
            imageUrl = "https://images.unsplash.com/photo-1497366216548-37526070297c",
            ubicacion = "Floor 1 - Room A",
            capacidad = 8,
            descripcion = "Executive office with panoramic view and full equipment"
        ),
        Office(
            id = 2,
            imageUrl = "https://images.unsplash.com/photo-1497366811353-6870744d04b2",
            ubicacion = "Floor 2 - Room B",
            capacidad = 12,
            descripcion = "Conference room with projector and video conferencing"
        ),
        Office(
            id = 3,
            imageUrl = "https://images.unsplash.com/photo-1497366754035-f200968a6e72",
            ubicacion = "Floor 3 - Room C",
            capacidad = 20,
            descripcion = "Spacious auditorium ideal for presentations and corporate events"
        ),
        Office(
            id = 4,
            imageUrl = "https://images.unsplash.com/photo-1542744173-8e7e53415bb0",
            ubicacion = "Floor 1 - Room D",
            capacidad = 6,
            descripcion = "Private meeting room with smart board and WiFi"
        ),
    )

    private val _officesFlow = kotlinx.coroutines.flow.MutableStateFlow(initial)
    val officesFlow: kotlinx.coroutines.flow.StateFlow<List<Office>> = _officesFlow

    fun getOffices(): List<Office> = _officesFlow.value

    fun getOfficeById(id: Int): Office? = _officesFlow.value.firstOrNull { it.id == id }

    fun addOffice(imageUrl: String, ubicacion: String, capacidad: Int, descripcion: String): Office {
        val nextId = (_officesFlow.value.maxOfOrNull { it.id } ?: 0) + 1
        val office = Office(
            id = nextId,
            imageUrl = imageUrl,
            ubicacion = ubicacion,
            capacidad = capacidad,
            descripcion = descripcion
        )
        _officesFlow.value = _officesFlow.value + office
        return office
    }

    fun updateOffice(id: Int, imageUrl: String, ubicacion: String, capacidad: Int, descripcion: String): Office? {
        var updated: Office? = null
        _officesFlow.value = _officesFlow.value.map { existing ->
            if (existing.id == id) {
                updated = existing.copy(
                    imageUrl = imageUrl,
                    ubicacion = ubicacion,
                    capacidad = capacidad,
                    descripcion = descripcion
                )
                updated!!
            } else existing
        }
        return updated
    }
}
