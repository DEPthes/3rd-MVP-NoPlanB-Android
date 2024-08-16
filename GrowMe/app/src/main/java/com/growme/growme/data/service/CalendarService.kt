package com.growme.growme.data.service

import com.growme.growme.data.model.BaseResponse
import com.growme.growme.data.model.MessageResponseDTO
import com.growme.growme.data.model.calendar.AddFutureQuestRequestDTO
import com.growme.growme.data.model.calendar.GetMonthExpResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CalendarService {

    @POST("/api/v1/calendar")
    suspend fun addFutureQuest(
        @Header("Authorization") accessToken: String,
        @Body body: AddFutureQuestRequestDTO
    ): Response<BaseResponse<MessageResponseDTO>>

    @GET("/api/v1/calendar/{date}")
    suspend fun getCalendarExp(
        @Header("Authorization") accessToken: String,
        @Path("date") date: String
    ): Response<BaseResponse<GetMonthExpResponseDTO>>
}