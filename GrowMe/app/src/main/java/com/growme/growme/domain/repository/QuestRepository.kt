package com.growme.growme.domain.repository

import com.growme.growme.domain.model.CUDQuestInfo
import com.growme.growme.domain.model.HomeInfo
import com.growme.growme.domain.model.QuestInfo

interface QuestRepository {

    suspend fun fetchHomeDate(
        accessToken: String
    ): Result<HomeInfo>

    suspend fun addQuest(
        accessToken: String,
        contents: String,
        exp: Int
    ): Result<CUDQuestInfo>

    suspend fun updateQuest(
        accessToken: String,
        id: Int,
        contents: String
    ): Result<CUDQuestInfo>

    suspend fun getQuest(
        accessToken: String,
        date: String
    ): Result<QuestInfo>

    suspend fun deleteQuest(
        accessToken: String,
        id: Int
    ): Result<CUDQuestInfo>
}