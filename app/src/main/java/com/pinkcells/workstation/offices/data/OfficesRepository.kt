package com.pinkcells.workstation.offices.data

import android.util.Log
import com.pinkcells.workstation.offices.domain.Office
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object OfficesRepository {
    private val _officesFlow = MutableStateFlow<List<Office>>(emptyList())
    val officesFlow: StateFlow<List<Office>> = _officesFlow.asStateFlow()

    suspend fun fetchOffices(): List<Office> {
        Log.d("OfficesRepository", "fetchOffices() - Iniciando llamada a la API...")

        return try {
            val response = RetrofitInstance.api.getOffices()

            Log.d("OfficesRepository", "Response code: ${response.code()}")
            Log.d("OfficesRepository", "Response isSuccessful: ${response.isSuccessful}")
            Log.d("OfficesRepository", "Response body: ${response.body()}")

            if (response.isSuccessful) {
                val offices = response.body() ?: emptyList()
                Log.d("OfficesRepository", "Oficina recibida  ${offices.size}")

                _officesFlow.value = offices
                offices
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("OfficesRepository", "Errror ${response.code()}: $errorBody")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("OfficesRepository", "No se llamo a la API ${e.message}", e)
            Log.e("OfficesRepository", "Stack trace: ${e.stackTraceToString()}")
            emptyList()
        }
    }
    suspend fun fetchOfficeById(id: String): Office? {
        Log.d("OfficesRepository", "fetchOfficeById($id) - Iniciando...")

        return try {
            val response = RetrofitInstance.api.getOfficeById(id)

            Log.d("OfficesRepository", "getOfficeById Response code: ${response.code()}")

            if (response.isSuccessful) {
                val office = response.body()
                Log.d("OfficesRepository", "Oficina encontrada: $office")
                office
            } else {
                Log.e("OfficesRepository", "Error obteniendo oficina: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("OfficesRepository", "Excepción al obtener oficina: ${e.message}", e)
            null
        }
    }

    // Crear una nueva oficina en la API
    suspend fun addOffice(office: Office): Office? {
        Log.d("OfficesRepository", "addOffice() - Creando oficina: $office")

        return try {
            val response = RetrofitInstance.api.createOffice(office)

            Log.d("OfficesRepository", "createOffice Response code: ${response.code()}")

            if (response.isSuccessful) {
                val createdOffice = response.body()
                Log.d("OfficesRepository", "Oficina creada exitosamente: $createdOffice")

                // Refrescar la lista
                fetchOffices()
                createdOffice
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("OfficesRepository", "Error creando oficina: ${response.code()} - $errorBody")
                null
            }
        } catch (e: Exception) {
            Log.e("OfficesRepository", "Excepción al crear oficina: ${e.message}", e)
            null
        }
    }

    // Actualizar una oficina existente
    suspend fun updateOffice(office: Office): Office? {
        Log.d("OfficesRepository", "updateOffice() - Actualizando oficina: ${office.id}")

        return try {
            if (office.id == null) {
                Log.e("OfficesRepository", "No se puede actualizar: ID es null")
                return null
            }

            val response = RetrofitInstance.api.updateOffice(office.id, office)

            Log.d("OfficesRepository", "updateOffice Response code: ${response.code()}")

            if (response.isSuccessful) {
                val updatedOffice = response.body()
                Log.d("OfficesRepository", "Oficina actualizada exitosamente: $updatedOffice")

                // Refrescar la lista
                fetchOffices()
                updatedOffice
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("OfficesRepository", "Error actualizando oficina: ${response.code()} - $errorBody")
                null
            }
        } catch (e: Exception) {
            Log.e("OfficesRepository", "Excepción al actualizar oficina: ${e.message}", e)
            null
        }
    }
}