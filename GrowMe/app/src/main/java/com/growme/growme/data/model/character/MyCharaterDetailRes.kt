package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class MyCharaterDetailRes(
    @SerializedName("itemImage")
    val itemImage: String,
    @SerializedName("itemType")
    val itemType: String,
    @SerializedName("itemId")
    val itemId: Int
)