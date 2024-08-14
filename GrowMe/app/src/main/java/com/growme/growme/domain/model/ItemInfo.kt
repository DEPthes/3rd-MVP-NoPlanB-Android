package com.growme.growme.domain.model

import com.growme.growme.data.model.item.CategoryItem

data class ItemInfo<T>(
    val categoryItemList: T
)