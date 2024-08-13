package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.quest.AddQuestRequestDTO
import com.growme.growme.data.model.quest.UpdateQuestRequestDTO
import com.growme.growme.data.service.QuestService
import com.growme.growme.domain.model.CUDQuestInfo
import com.growme.growme.domain.model.HomeInfo
import com.growme.growme.domain.model.QuestInfo
import com.growme.growme.domain.repository.QuestRepository
import org.json.JSONObject
import java.io.IOException
import kotlin.math.exp

class QuestRepositoryImpl : QuestRepository {
    private val service = RetrofitClient.getInstance().create(QuestService::class.java)
    override suspend fun fetchHomeDate(accessToken: String): Result<HomeInfo> {
        val response = service.fetchHomeData(accessToken)

        return if (response.isSuccessful) {
            val item = response.body().run {
                HomeInfo(
                    level = this!!.level,
                    acquireExp = this.acquireExp,
                    needExp = this.needExp,
                    todayExp = this.todayExp
                )
            }
            Result.success(item)
        } else {
            val errorBody = response.errorBody()
            val errorMsg = if (errorBody != null) {
                try {
                    JSONObject(errorBody.string()).getString("msg")
                } catch (e: Exception) {
                    "An unknown error occurred"
                }
            } else {
                "Error body is null"
            }
            Result.failure(IOException(errorMsg))
        }
    }

    override suspend fun addQuest(
        accessToken: String,
        contents: String,
        exp: Int
    ): Result<CUDQuestInfo> {
        val response = service.addQuest(accessToken, AddQuestRequestDTO(contents, exp))

        return if (response.isSuccessful) {
            Result.success(CUDQuestInfo(response.body()!!.message))
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun updateQuest(
        accessToken: String,
        id: Int,
        contents: String
    ): Result<CUDQuestInfo> {
        val response = service.updateQuest(accessToken, UpdateQuestRequestDTO(id, contents))

        return if (response.isSuccessful) {
            Result.success(CUDQuestInfo(response.body()!!.message))
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

    override suspend fun deleteQuest(accessToken: String, id: Int): Result<CUDQuestInfo> {
        val response = service.deleteQuest(accessToken, id)

        return if (response.isSuccessful) {
            Result.success(CUDQuestInfo(response.body()!!.message))
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }
}