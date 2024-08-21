package com.growme.growme.data.repository

import com.growme.growme.data.LoggerUtils
import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.service.UserService
import com.growme.growme.domain.model.IsUserRegisteredInfo
import com.growme.growme.domain.model.MessageInfo
import com.growme.growme.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val service = RetrofitClient.getInstance().create(UserService::class.java)
    private val dataStoreRepositoryImpl = DataStoreRepositoryImpl()

    override suspend fun getUserEmail(): Result<MessageInfo> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getUserEmail("Bearer $accessToken")

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.email))
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun isUserRegistered(): Result<IsUserRegisteredInfo> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.isUserRegistered("Bearer $accessToken")

        return if (response.isSuccessful) {
            Result.success(IsUserRegisteredInfo(response.body()!!.information!!.exist))
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun withdraw(): Result<MessageInfo> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.withdraw("Bearer $accessToken")

        return if (response.isSuccessful) {
            Result.success(MessageInfo(response.body()!!.information))
        } else {
            Result.failure(Exception("response failure"))
        }
    }
}