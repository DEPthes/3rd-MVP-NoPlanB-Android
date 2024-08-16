package com.growme.growme.data.repository

import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.quest.AddQuestRequestDTO
import com.growme.growme.data.model.quest.UpdateQuestRequestDTO
import com.growme.growme.data.service.QuestService
import com.growme.growme.domain.model.home.HomeExpInfo
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.quest.QuestInfo
import com.growme.growme.domain.repository.CompleteQuestInfo
import com.growme.growme.domain.repository.QuestRepository
import org.json.JSONObject

class QuestRepositoryImpl : QuestRepository {
    private val service = RetrofitClient.getInstance().create(QuestService::class.java)
    private val userPreferencesRepositoryImpl = UserPreferencesRepositoryImpl()

    override suspend fun fetchHomeData(): Result<HomeExpInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
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
        contents: String,
        exp: Int
    ): Result<MessageInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.addQuest("Bearer $accessToken", AddQuestRequestDTO(contents, exp))

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.information!!.message))
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun updateQuest(
        id: Int,
        contents: String
    ): Result<MessageInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response =
            service.updateQuest("Bearer $accessToken", UpdateQuestRequestDTO(id, contents))

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.information!!.message))
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun completeQuest(id: Int): Result<CompleteQuestInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.completeQuest("Bearer $accessToken", id)

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information
                if (data != null) {
                    val itemList = data.itemImageUrls.listIterator()
                    val newItemImageUrls = mutableListOf<String>()
                    while (itemList.hasNext()) {
                        newItemImageUrls.add(itemList.next())
                    }

                    val completeQuestInfo = CompleteQuestInfo(
                        itemImageUrls = newItemImageUrls,
                        questType = data.questType
                    )
                    Result.success(completeQuestInfo)
                } else {
                    Result.failure(Exception("Complete Quest data is null"))
                }
            } else {
                Result.failure(Exception("Complete Quest Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getQuest(date: String): Result<List<QuestInfo>> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getQuest("Bearer $accessToken", date)

        return if (response.isSuccessful) {
            val items = response.body()?.information?.map {
                QuestInfo(
                    id = it.id,
                    exp = it.exp,
                    contents = it.contents,
                    isComplete = it.isComplete
                )
            } ?: emptyList() // body가 null인 경우 빈 리스트 반환

            Result.success(items)
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun deleteQuest(id: Int): Result<MessageInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.deleteQuest("Bearer $accessToken", id)

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.information!!.message))
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }
}