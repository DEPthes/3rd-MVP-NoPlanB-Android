package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.item.CategoryItem
import com.growme.growme.data.service.ItemService
import com.growme.growme.domain.model.ItemInfo
import com.growme.growme.domain.repository.ItemRepository

class ItemRepositoryImpl : ItemRepository {
    private val service = RetrofitClient.getInstance().create(ItemService::class.java)
    private val dataStoreRepositoryImpl = DataStoreRepositoryImpl()

    override suspend fun getHairItems(): Result<ItemInfo<List<CategoryItem>>> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getHairItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information?.categoryItemList
                if (data != null) {
                    Result.success(ItemInfo(data))
                } else {
                    Result.failure(Exception("Item Category data is null"))
                }
            } else {
                Result.failure(Exception("Item Category fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getFashionItems(): Result<ItemInfo<List<CategoryItem>>> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getFashionItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information?.categoryItemList
                if (data != null) {
                    Result.success(ItemInfo(data))
                } else {
                    Result.failure(Exception("Item Category data is null"))
                }
            } else {
                Result.failure(Exception("Item Category fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getFaceItems(): Result<ItemInfo<List<CategoryItem>>> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getFaceItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information?.categoryItemList
                if (data != null) {
                    Result.success(ItemInfo(data))
                } else {
                    Result.failure(Exception("Item Category data is null"))
                }
            } else {
                Result.failure(Exception("Item Category fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getBackgroundItems(): Result<ItemInfo<List<CategoryItem>>> {
        val accessToken = dataStoreRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getBackgroundItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.information?.categoryItemList
                if (data != null) {
                    Result.success(ItemInfo(data))
                } else {
                    Result.failure(Exception("Item Category data is null"))
                }
            } else {
                Result.failure(Exception("Item Category fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }
}