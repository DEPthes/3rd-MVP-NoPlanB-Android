package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.auth.GetAccessTokenRequestDTO
import com.growme.growme.data.model.auth.GetAccessTokenResponseDTO
import com.growme.growme.data.model.user.UserEmailResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @GET("/api/v1/user/account")
    suspend fun getUserEmail(
        @Header("Authorization") accessToken: String
    ): Response<UserEmailResponseDTO>
}