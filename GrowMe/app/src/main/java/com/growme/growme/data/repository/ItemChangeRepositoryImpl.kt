package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.service.ItemChangeService
import com.growme.growme.domain.model.character.ItemchangeInfo
import com.growme.growme.domain.repository.ItemChangeRepository

class ItemChangeRepositoryImpl: ItemChangeRepository {
    private val service = RetrofitClient.getInstance().create(ItemChangeService::class.java)
    private val userPreferencesRepositoryImpl = UserPreferencesRepositoryImpl()
    override suspend fun changeItem(): Result<ItemchangeInfo> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
//        val response = service.changeItem("Bearer ${accessToken}")

//        return if (response.isSuccessful) {
//            val res = response.body()
//            if (res != null) {
//                val data = res.information
//                if (data != null) {
//
//                }
//            }
//        }
        TODO()
    }
}