package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class MakeInitCharacterDTO(
    @SerializedName("characterName")
    val characterName: String,
    @SerializedName("itemIdList")
    val itemIdList: List<Int>
)