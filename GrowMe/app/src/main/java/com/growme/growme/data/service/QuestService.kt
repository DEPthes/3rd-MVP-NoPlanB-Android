package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.quest.AddQuestRequestDTO
import com.growme.growme.data.model.quest.CUDQuestResponseDTO
import com.growme.growme.data.model.quest.MainResponseDTO
import com.growme.growme.data.model.quest.QuestResponseDTO
import com.growme.growme.data.model.quest.UpdateQuestRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestService {

    @GET("/api/v1/quest")
    suspend fun fetchHomeData(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<MainResponseDTO>>

    @POST("/quest")
    suspend fun addQuest(
        @Header("Authorization") accessToken: String,
        @Body body: AddQuestRequestDTO
    ): Response<CUDQuestResponseDTO>

    @PATCH("/quest")
    suspend fun updateQuest(
        @Header("Authorization") accessToken: String,
        @Body body: UpdateQuestRequestDTO
    ): Response<CUDQuestResponseDTO>

    @GET("/quest/{date}")
    suspend fun getQuest(
        @Header("Authorization") accessToken: String,
        @Path("date") data: String
    ): Response<QuestResponseDTO>


    @DELETE("/quest/{id}")
    suspend fun deleteQuest(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ): Response<CUDQuestResponseDTO>
}