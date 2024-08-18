package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.calendar.AddFutureQuestRequestDTO
import com.growme.growme.data.service.CalendarService
import com.growme.growme.domain.model.calendar.GetMonthExpInfoItem
import com.growme.growme.domain.repository.CalendarRepository

class CalendarRepositoryImpl : CalendarRepository {
    private val service = RetrofitClient.getInstance().create(CalendarService::class.java)
    private val userPreferencesRepositoryImpl = UserPreferencesRepositoryImpl()
    override suspend fun addFutureQuest(date: String, contents: String, exp: Int): Result<String> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.addFutureQuest(
            "Bearer $accessToken",
            AddFutureQuestRequestDTO(contents, date, exp)
        )

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information
                if (data != null) {
                    Result.success(data.message)
                } else {
                    Result.failure(Exception("information data is null"))
                }
            } else {
                Result.failure(Exception("Add Future Quest Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getCalendarExp(date: String): Result<List<GetMonthExpInfoItem>> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getCalendarExp("Bearer $accessToken", date)

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information
                if (data != null) {
                    val monthExpResult = data.map {
                        GetMonthExpInfoItem(
                            date = it.date,
                            exp = it.exp
                        )
                    }

                    Result.success(monthExpResult)
                } else {
                    Result.failure(Exception("Month Exp Info data is null"))
                }
            } else {
                Result.failure(Exception("Month Exp Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }
}