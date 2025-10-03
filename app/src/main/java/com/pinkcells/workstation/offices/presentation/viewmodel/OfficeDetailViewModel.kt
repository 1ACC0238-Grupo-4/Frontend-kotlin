package com.pinkcells.workstation.offices.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pinkcells.workstation.offices.data.OfficesRepository
import com.pinkcells.workstation.offices.presentation.components.Office
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OfficeDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _office = MutableStateFlow<Office?>(null)
    val office: StateFlow<Office?> = _office

    init {
        val id: Int? = savedStateHandle["officeId"]
        _office.value = id?.let { OfficesRepository.getOfficeById(it) }
    }
}
