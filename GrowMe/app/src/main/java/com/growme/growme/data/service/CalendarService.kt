package com.growme.growme.data.service

import com.growme.growme.data.model.calendar.GetCalendarResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CalendarService {

    @GET("/calendar/{date}")
    suspend fun getCalendarExp(
        @Header("Authorization") accessToken: String,
        @Path("date") date: String
    ): Response<GetCalendarResponseDTO>

}