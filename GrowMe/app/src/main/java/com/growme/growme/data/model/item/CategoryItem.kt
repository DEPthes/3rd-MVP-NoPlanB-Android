package com.growme.growme.data.model.item

data class CategoryItem(
    val ableToEquip: Boolean,
    val equipped: Boolean,
    val itemId: Int,
    val itemImage: String,
    val itemName: String,
    val itemType: String,
    val requiredLevel: Int
)