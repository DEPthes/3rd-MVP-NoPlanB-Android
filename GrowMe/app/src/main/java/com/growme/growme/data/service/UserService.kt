package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.MessageResponseDTO
import com.growme.growme.data.model.user.IsUserRegisteredResponseDTO
import com.growme.growme.data.model.user.UserEmailResponseDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("/api/v1/user/account")
    suspend fun getUserEmail(
        @Header("Authorization") accessToken: String,
    ): Response<UserEmailResponseDTO>

    @GET("/api/v1/user/character-exist")
    suspend fun isUserRegistered(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<IsUserRegisteredResponseDTO>>

    @DELETE("/api/v1/user/")
    suspend fun withdraw(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<MessageResponseDTO>>
}