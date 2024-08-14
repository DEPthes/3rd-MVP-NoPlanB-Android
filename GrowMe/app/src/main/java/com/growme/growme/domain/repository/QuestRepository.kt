package com.growme.growme.domain.repository

import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.HomeExpInfo
import com.growme.growme.domain.model.quest.QuestInfo

interface QuestRepository {

    suspend fun fetchHomeData(): Result<HomeExpInfo>

    suspend fun addQuest(
        contents: String,
        exp: Int
    ): Result<MessageInfo>

    suspend fun updateQuest(
        id: Int,
        contents: String
    ): Result<MessageInfo>

    suspend fun completeQuest(id: Int): Result<MessageInfo>

    suspend fun getQuest(date: String): Result<List<QuestInfo>>

    suspend fun deleteQuest(id: Int): Result<MessageInfo>
}