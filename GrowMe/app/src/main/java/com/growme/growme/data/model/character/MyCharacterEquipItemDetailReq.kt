package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class MyCharacterEquipItemDetailReq(
    @SerializedName("itemType")
    val itemType: String,
    @SerializedName("itemId")
    val itemId: Int
)