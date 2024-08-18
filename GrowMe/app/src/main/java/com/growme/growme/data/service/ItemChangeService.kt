package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.MessageResponseDTO
import com.growme.growme.data.model.character.ItemChangeResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ItemChangeService {
    @POST("api/v1/character/equip")
    suspend fun changeItem(
        @Header("Authorization") accessToken: String,
        @Body body: ItemChangeResponseDTO
    ): Response<BaseResponse<MessageResponseDTO>>
}