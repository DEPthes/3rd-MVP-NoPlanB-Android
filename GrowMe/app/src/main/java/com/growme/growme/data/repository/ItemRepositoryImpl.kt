package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.service.ItemService
import com.growme.growme.domain.model.ItemInfo
import com.growme.growme.domain.repository.ItemRepository
import org.json.JSONObject

class ItemRepositoryImpl : ItemRepository {
    private val service = RetrofitClient.getInstance().create(ItemService::class.java)

    override suspend fun getHairItems(): Result<ItemInfo> {
        val response = service.getHairItems()

        return if (response.isSuccessful) {
            val item = response.body().run {
                ItemInfo(
                    categoryItemList = this!!.categoryItemList
                )
            }
            Result.success(item)
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun getFashionItems(): Result<ItemInfo> {
        val response = service.getFashionItems()

        return if (response.isSuccessful) {
            val item = response.body().run {
                ItemInfo(
                    categoryItemList = this!!.categoryItemList
                )
            }
            Result.success(item)
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun getFaceItems(): Result<ItemInfo> {
        val response = service.getFaceItems()

        return if (response.isSuccessful) {
            val item = response.body().run {
                ItemInfo(
                    categoryItemList = this!!.categoryItemList
                )
            }
            Result.success(item)
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun getBackgroundItems(): Result<ItemInfo> {
        val response = service.getBackgroundItems()

        return if (response.isSuccessful) {
            val item = response.body().run {
                ItemInfo(
                    categoryItemList = this!!.categoryItemList
                )
            }
            Result.success(item)
        } else {
            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("msg")
            Result.failure(Exception(errorMsg))
        }
    }
}