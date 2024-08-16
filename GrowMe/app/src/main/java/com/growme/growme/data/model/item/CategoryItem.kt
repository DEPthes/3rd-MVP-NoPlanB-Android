package com.growme.growme.data.model.item

data class CategoryItem(
    val itemId: Int,
    val itemName: String,
    val itemImage: String,
    val itemType: String,
    val ableToEquip: Boolean,
    val requiredLevel: Int,
    val equipped: Boolean
)