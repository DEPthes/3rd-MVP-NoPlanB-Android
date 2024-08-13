package com.growme.growme.data.service

import com.growme.growme.data.model.character.ChangeNicknameRequestDTO
import com.growme.growme.data.model.character.ChangeNicknameResponseDTO
import com.growme.growme.data.model.character.CharacterInfoResponseDTO
import com.growme.growme.data.model.character.GetCharacterItemResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CharacterService {

    @GET("/character/my")
    suspend fun getCharacterInfo(
        @Header("Authorization") accessToken: String
    ): Response<CharacterInfoResponseDTO>


    @POST("/character/my")
    suspend fun changeNickname(
        @Header("Authorization") accessToken: String,
        @Body body: ChangeNicknameRequestDTO
    ): Response<ChangeNicknameResponseDTO>


    @GET("/character/")
    suspend fun getCharacterItem(
        @Header("Authorization") accessToken: String
    ): Response<GetCharacterItemResponseDTO>

}