package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.service.CharacterService
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.model.character.CharacterInitialInfo
import com.growme.growme.domain.model.character.GetCharacterItemInfo
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
                    val myCharacterResult = data.myCharaterDetailResList.map { detailResItem ->
                        MyCharacterDetailInfo(
                            itemImage = detailResItem.itemImage,
                            itemType = detailResItem.itemType,
                            itemId = detailResItem.itemId
                        )
                    }

                    val myPageInfo = MyPageInfo(
                        characterName = data.characterName,
                        growDate = data.growDate,
                        myCharaterDetailResList = myCharacterResult,
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
        TODO("닉네임 변경 기능")
    }

    override suspend fun getInitialInfo(): Result<CharacterInitialInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getInitialInfo("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information
                if (data != null) {
                    val myCharacterResult = data.myCharaterDetailResList.map { detailResItem ->
                        MyCharacterDetailInfo(
                            itemImage = detailResItem.itemImage,
                            itemType = detailResItem.itemType,
                            itemId = detailResItem.itemId
                        )
                    }

                    val initialCharacterInfo = CharacterInitialInfo(
                        characterName = data.characterName,
                        myCharaterDetailResList = myCharacterResult
                    )
                    Result.success(initialCharacterInfo)
                } else {
                    Result.failure(Exception("Initial Info data is null"))
                }
            } else {
                Result.failure(Exception("Initial Info fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getCharacterItem(): Result<GetCharacterItemInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getCharacterItem("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information
                if (data != null) {
                    val myCharacterResult = data.myCharaterDetailResList.map { detailResItem ->
                        MyCharacterDetailInfo(
                            itemImage = detailResItem.itemImage,
                            itemType = detailResItem.itemType,
                            itemId = detailResItem.itemId
                        )
                    }

                    val characterItemInfo = GetCharacterItemInfo(
                        myCharaterDetailResList = myCharacterResult
                    )
                    Result.success(characterItemInfo)
                } else {
                    Result.failure(Exception("Get Character Info: data is null"))
                }
            } else {
                Result.failure(Exception("Get Character Info Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }
}