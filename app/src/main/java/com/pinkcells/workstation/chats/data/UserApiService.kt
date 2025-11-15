package com.pinkcells.workstation.chats.data

import com.pinkcells.workstation.chats.domain.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("user")
    suspend fun getUsers(): Response<List<User>>
}