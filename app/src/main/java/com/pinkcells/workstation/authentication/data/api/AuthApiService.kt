package com.pinkcells.workstation.authentication.data.api

import com.pinkcells.workstation.authentication.data.models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/workstation/User/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @POST("/api/workstation/User/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<ResponseBody>

    @POST("/api/workstation/User/forgot-password")
    suspend fun requestPasswordReset(@Body request: PasswordResetRequest): Response<ResponseBody>

    @GET("/api/workstation/User")
    suspend fun getAllUsers(): Response<List<User>>
}