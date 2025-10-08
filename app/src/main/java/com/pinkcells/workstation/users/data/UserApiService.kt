package com.pinkcells.workstation.users.data

import com.pinkcells.workstation.users.domain.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {

    @GET("User")
    suspend fun getUsers(): Response<List<User>>

    @GET("User/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>

    @PUT("User/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>

    @DELETE("User/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>
}