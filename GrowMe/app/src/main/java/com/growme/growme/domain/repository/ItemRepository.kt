package com.growme.growme.domain.repository

import com.growme.growme.data.model.item.CategoryItem
import com.growme.growme.domain.model.ItemInfo

interface ItemRepository {
    suspend fun getHairItems() : Result<ItemInfo<List<CategoryItem>>>

    suspend fun getFashionItems() : Result<ItemInfo<List<CategoryItem>>>

    suspend fun getFaceItems() : Result<ItemInfo<List<CategoryItem>>>

    suspend fun getBackgroundItems() : Result<ItemInfo<List<CategoryItem>>>
}