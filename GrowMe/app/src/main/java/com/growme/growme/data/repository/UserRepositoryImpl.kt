package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.quest.AddQuestRequestDTO
import com.growme.growme.data.service.QuestService
import com.growme.growme.data.service.UserService
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val service = RetrofitClient.getInstance().create(UserService::class.java)
    private val userPreferencesRepositoryImpl = UserPreferencesRepositoryImpl()

    override suspend fun getUserEmail(): Result<MessageInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getUserEmail("Bearer $accessToken")

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.email))
        } else {
            Result.failure(Exception("response failure"))
        }
    }

}