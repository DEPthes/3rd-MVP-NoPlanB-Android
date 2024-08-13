package com.growme.growme.data.service

import com.growme.growme.data.model.auth.GetAccessTokenRequestDTO
import com.growme.growme.data.model.auth.GetAccessTokenResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/idTokenLogin")
    suspend fun login(
        @Body body: GetAccessTokenRequestDTO
    ): Response<GetAccessTokenResponseDTO>
}