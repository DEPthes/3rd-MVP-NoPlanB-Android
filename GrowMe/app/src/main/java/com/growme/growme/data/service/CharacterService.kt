package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.character.ChangeNicknameRequestDTO
import com.growme.growme.data.model.character.ChangeNicknameResponseDTO
import com.growme.growme.data.model.character.CharacterInfoResponseDTO
import com.growme.growme.data.model.character.GetCharacterItemResponseDTO
import com.growme.growme.data.model.character.GetInitialResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CharacterService {

    @GET("api/v1/character/my")
    suspend fun getCharacterInfo(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<CharacterInfoResponseDTO>>


    @POST("api/v1/character/my")
    suspend fun changeNickname(
        @Header("Authorization") accessToken: String,
        @Body body: ChangeNicknameRequestDTO
    ): Response<BaseResponse<ChangeNicknameResponseDTO>>

    @GET("/api/v1/character/initial/info")
    suspend fun getInitialInfo(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<GetInitialResponseDTO>>


    @GET("api/v1/character/")
    suspend fun getCharacterItem(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<GetCharacterItemResponseDTO>>
}