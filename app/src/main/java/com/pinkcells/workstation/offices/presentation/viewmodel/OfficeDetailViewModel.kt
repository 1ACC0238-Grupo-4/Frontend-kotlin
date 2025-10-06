package com.pinkcells.workstation.offices.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcells.workstation.offices.data.OfficesRepository
import com.pinkcells.workstation.offices.domain.Office
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OfficeDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _office = MutableStateFlow<Office?>(null)
    val office: StateFlow<Office?> = _office

    init {
        val id: String? = savedStateHandle["officeId"]
        if (id != null) {
            fetchOfficeById(id)
        }
    }

    fun fetchOfficeById(id: String) {
        viewModelScope.launch {
            _office.value = OfficesRepository.fetchOfficeById(id)
        }
    }

    fun saveOffice(office: Office, onResult: (Office?) -> Unit = {}) {
        viewModelScope.launch {
            val result = OfficesRepository.addOffice(office)
            onResult(result)
        }
    }
}
