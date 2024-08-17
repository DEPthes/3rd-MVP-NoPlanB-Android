package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class MyCharacterEquipItemDetailReq(
    @SerializedName("itemId")
    val itemId: Int,
    @SerializedName("itemType")
    val itemType: String
)