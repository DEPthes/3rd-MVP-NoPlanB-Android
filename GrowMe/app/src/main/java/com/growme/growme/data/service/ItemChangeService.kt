package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.character.ChangeNicknameResponseDTO
import com.growme.growme.data.model.item.MyCharacterEquipItemDetailReq
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ItemChangeService {
    @POST("api/v1/character/equip")
    suspend fun changeItem(
        @Header("Authorization") accessToken: String,
        @Query("myCharacterEquipItemReq") myCharacterEquipItemReq: List<MyCharacterEquipItemDetailReq>
    ): Response<BaseResponse<ChangeNicknameResponseDTO>>
}