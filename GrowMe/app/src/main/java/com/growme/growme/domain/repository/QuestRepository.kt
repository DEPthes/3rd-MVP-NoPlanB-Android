package com.growme.growme.domain.repository

import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.HomeExpInfo
import com.growme.growme.domain.model.QuestInfo

interface QuestRepository {

    suspend fun fetchHomeData(): Result<HomeExpInfo>

    suspend fun addQuest(
        accessToken: String,
        contents: String,
        exp: Int
    ): Result<MessageInfo>

    suspend fun updateQuest(
        accessToken: String,
        id: Int,
        contents: String
    ): Result<MessageInfo>

    suspend fun getQuest(
        accessToken: String,
        date: String
    ): Result<QuestInfo>

    suspend fun deleteQuest(
        accessToken: String,
        id: Int
    ): Result<MessageInfo>
}