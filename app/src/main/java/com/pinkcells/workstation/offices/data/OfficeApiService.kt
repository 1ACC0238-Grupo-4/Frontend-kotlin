package com.pinkcells.workstation.offices.data

import com.pinkcells.workstation.offices.domain.Office
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OfficeApiService {
    @GET("office")
    suspend fun getOffices(): Response<List<Office>>

    @GET("office/{id}")
    suspend fun getOfficeById(@Path("id") id: String): Response<Office>

    @POST("office")
    suspend fun createOffice(@Body office: Office): Response<Office>

    @PUT("office/{id}")
    suspend fun updateOffice(@Path("id") id: String, @Body office: Office): Response<Office>

    @DELETE("office/{id}")
    suspend fun deleteOffice(@Path("id") id: String): Response<Unit>

}

