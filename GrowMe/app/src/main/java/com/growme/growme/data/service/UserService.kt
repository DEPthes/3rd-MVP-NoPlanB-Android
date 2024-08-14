package com.growme.growme.data.service

import com.growme.growme.data.model.user.UserEmailResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("/api/v1/user/account")
    suspend fun getUserEmail(
        @Header("Authorization") accessToken: String
    ): Response<UserEmailResponseDTO>
}