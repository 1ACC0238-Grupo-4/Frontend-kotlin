package com.pinkcells.workstation.offices.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcells.workstation.offices.data.OfficesRepository
import com.pinkcells.workstation.offices.domain.Office
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import kotlinx.coroutines.flow.asStateFlow

class OfficesViewModel : ViewModel() {
    private val _offices = MutableStateFlow<List<Office>>(emptyList())
    val offices: StateFlow<List<Office>> = _offices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchOffices()
    }

    fun fetchOffices() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            Log.d("OfficesViewModel", "Starting to fetch offices...")

            try {
                val response = OfficesRepository.fetchOffices()

                Log.d("OfficesViewModel", "API Response received: $response")
                Log.d("OfficesViewModel", "Number of offices: ${response.size}")

                _offices.value = response

                Log.d("OfficesViewModel", "Offices updated in StateFlow: ${_offices.value}")

            } catch (e: Exception) {
                Log.e("OfficesViewModel", "Error fetching offices: ${e.message}", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
                Log.d("OfficesViewModel", "Loading finished. isLoading = ${_isLoading.value}")
            }
        }
    }

    fun addOffice(office: Office, onResult: (Office?) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = OfficesRepository.addOffice(office)
            _isLoading.value = false
            onResult(result)
        }
    }
}
