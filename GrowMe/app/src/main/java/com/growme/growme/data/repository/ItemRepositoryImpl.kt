package com.growme.growme.data.repository

import com.growme.growme.data.RetrofitClient
import com.growme.growme.data.model.item.CategoryItem
import com.growme.growme.data.service.ItemService
import com.growme.growme.domain.model.ItemInfo
import com.growme.growme.domain.repository.ItemRepository
import org.json.JSONObject

class ItemRepositoryImpl : ItemRepository {
    private val service = RetrofitClient.getInstance().create(ItemService::class.java)
    private val userPreferencesRepositoryImpl = UserPreferencesRepositoryImpl()

    override suspend fun getHairItems(): Result<ItemInfo<CategoryItem>> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getHairItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.categoryItemList
                if (data != null) {
                    val item = CategoryItem(
                        itemId = data.itemId,
                        itemName = data.itemName,
                        itemImage = data.itemImage,
                        itemType = data.itemType,
                        ableToEquip = data.ableToEquip,
                        requiredLevel = data.requiredLevel,
                        equipped = data.equipped
                    )
                    Result.success(ItemInfo(item))
                } else {
                    Result.failure(Exception("Item Info data is null"))
                }
            } else {
                Result.failure(Exception("Item Info fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getFashionItems(): Result<ItemInfo<CategoryItem>> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getFashionItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.categoryItemList
                if (data != null) {
                    val item = CategoryItem(
                        itemId = data.itemId,
                        itemName = data.itemName,
                        itemImage = data.itemImage,
                        itemType = data.itemType,
                        ableToEquip = data.ableToEquip,
                        requiredLevel = data.requiredLevel,
                        equipped = data.equipped
                    )
                    Result.success(ItemInfo(item))
                } else {
                    Result.failure(Exception("Item Info data is null"))
                }
            } else {
                Result.failure(Exception("Item Info fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getFaceItems(): Result<ItemInfo<CategoryItem>> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getFaceItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.categoryItemList
                if (data != null) {
                    val item = CategoryItem(
                        itemId = data.itemId,
                        itemName = data.itemName,
                        itemImage = data.itemImage,
                        itemType = data.itemType,
                        ableToEquip = data.ableToEquip,
                        requiredLevel = data.requiredLevel,
                        equipped = data.equipped
                    )
                    Result.success(ItemInfo(item))
                } else {
                    Result.failure(Exception("Item Info data is null"))
                }
            } else {
                Result.failure(Exception("Item Info fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }

    override suspend fun getBackgroundItems(): Result<ItemInfo<CategoryItem>> {
        val accessToken = userPreferencesRepositoryImpl.getAccessToken().getOrNull()
        val response = service.getBackgroundItems("Bearer $accessToken")

        return if (response.isSuccessful) {
            val res = response.body()
            if (res != null) {
                val data = res.categoryItemList
                if (data != null) {
                    val item = CategoryItem(
                        itemId = data.itemId,
                        itemName = data.itemName,
                        itemImage = data.itemImage,
                        itemType = data.itemType,
                        ableToEquip = data.ableToEquip,
                        requiredLevel = data.requiredLevel,
                        equipped = data.equipped
                    )
                    Result.success(ItemInfo(item))
                } else {
                    Result.failure(Exception("Item Info data is null"))
                }
            } else {
                Result.failure(Exception("Item Info fetch Failed: response body is null"))
            }
        } else {
            Result.failure(Exception("response failure"))
        }
    }
}