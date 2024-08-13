package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class CharacterInfoResponseDTO(
    @SerializedName("characterName")
    val characterName: String,
    @SerializedName("growDate")
    val growDate: Int,
    @SerializedName("myCharaterDetailResList")
    val myCharaterDetailResList: List<MyCharaterDetailRes>,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("totalExp")
    val totalExp: Int,
    @SerializedName("totalQuest")
    val totalQuest: Int
)