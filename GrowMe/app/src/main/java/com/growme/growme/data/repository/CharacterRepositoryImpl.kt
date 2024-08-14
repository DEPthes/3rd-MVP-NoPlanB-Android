package com.growme.growme.data.repository

import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.service.CharacterService
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.character.MyCharacterDetailInfo
import com.growme.growme.domain.model.character.MyPageInfo
import com.growme.growme.domain.repository.CharacterRepository

class CharacterRepositoryImpl : CharacterRepository {
    private val service = RetrofitClient.getInstance().create(CharacterService::class.java)
    private val userPreferencesRepositoryImpl = UserPreferencesRepositoryImpl()

    override suspend fun getCharacterInfo(): Result<MyPageInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getCharacterInfo("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information
                if (data != null) {
                    val myPageInfo = MyPageInfo(
                        characterName = data.characterName,
                        growDate = data.growDate,
                        myCharaterDetailResList = emptyList<MyCharacterDetailInfo>(),
                        startDate = data.startDate,
                        totalExp = data.totalExp,
                        totalQuest = data.totalQuest
                    )
                    Result.success(myPageInfo)
                } else {
                    Result.failure(Exception("MyPage Info data is null"))
                }
            } else {
                Result.failure(Exception("MyPage Info fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }

    }

    override suspend fun changeNickname(newName: String): Result<MessageInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacterItem(): Result<MyCharacterDetailInfo> {
        TODO("Not yet implemented")
    }
}