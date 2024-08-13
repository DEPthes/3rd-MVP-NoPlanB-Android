package com.growme.growme.domain.repository

import com.growme.growme.domain.model.ItemInfo

interface ItemRepository {
    suspend fun getHairItems() : Result<ItemInfo>

    suspend fun getFashionItems() : Result<ItemInfo>

    suspend fun getFaceItems() : Result<ItemInfo>

    suspend fun getBackgroundItems() : Result<ItemInfo>
}