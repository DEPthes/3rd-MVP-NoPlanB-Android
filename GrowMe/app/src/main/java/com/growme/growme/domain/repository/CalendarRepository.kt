package com.growme.growme.domain.repository

import com.growme.growme.domain.model.calendar.GetMonthExpInfo
import com.growme.growme.domain.model.calendar.GetMonthExpInfoItem

interface CalendarRepository {

    suspend fun addFutureQuest(
        date: String,
        contents: String,
        exp: Int
    ): Result<String>

    suspend fun getCalendarExp(date: String): Result<List<GetMonthExpInfoItem>>
}