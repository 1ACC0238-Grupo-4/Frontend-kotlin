package com.pinkcells.workstation.offices.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcells.workstation.offices.data.OfficesRepository
import com.pinkcells.workstation.offices.presentation.components.Office
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OfficesViewModel : ViewModel() {
    val offices: StateFlow<List<Office>> = OfficesRepository.officesFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // No-op: using in-memory repo. Keep flag false.
        _isLoading.value = false
    }

    fun addOffice(imageUrl: String, ubicacion: String, capacidad: Int, descripcion: String) {
        OfficesRepository.addOffice(imageUrl, ubicacion, capacidad, descripcion)
    }
}
