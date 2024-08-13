package com.growme.growme.data.repository

import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.quest.AddQuestRequestDTO
import com.growme.growme.data.model.quest.UpdateQuestRequestDTO
import com.growme.growme.data.service.QuestService
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.HomeExpInfo
import com.growme.growme.domain.model.QuestInfo
import com.growme.growme.domain.repository.QuestRepository
import org.json.JSONObject

class QuestRepositoryImpl : QuestRepository {
    private val service = RetrofitClient.getInstance().create(QuestService::class.java)
    private val userPreferencesRepositoryImpl = UserPreferencesRepositoryImpl()

    override suspend fun fetchHomeData(): Result<HomeExpInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        LoggerUtils.d(accessToken.toString())
        val response = service.fetchHomeData("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information
                if (data != null) {
                    val homeInfo = HomeExpInfo(
                        level = data.level,
                        acquireExp = data.acquireExp,
                        needExp = data.needExp,
                        todayExp = data.todayExp
                    )
                    Result.success(homeInfo)
                } else {
                    Result.failure(Exception("Home Info data is null"))
                }
            } else {
                Result.failure(Exception("Home Info fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }


    override suspend fun addQuest(
        accessToken: String,
        contents: String,
        exp: Int
    ): Result<MessageInfo> {
        val response = service.addQuest(accessToken, AddQuestRequestDTO(contents, exp))

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.message))
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun updateQuest(
        accessToken: String,
        id: Int,
        contents: String
    ): Result<MessageInfo> {
        val response = service.updateQuest(accessToken, UpdateQuestRequestDTO(id, contents))

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.message))
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun getQuest(accessToken: String, date: String): Result<QuestInfo> {
        val response = service.getQuest(accessToken, date)

        return if (response.isSuccessful) {
            val item = response.body().run {
                QuestInfo(
                    id = this!!.id,
                    exp = this.exp,
                    contents = this.contents,
                    isComplete = this.isComplete
                )
            }
            Result.success(item)
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun deleteQuest(accessToken: String, id: Int): Result<MessageInfo> {
        val response = service.deleteQuest(accessToken, id)

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.message))
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }
}