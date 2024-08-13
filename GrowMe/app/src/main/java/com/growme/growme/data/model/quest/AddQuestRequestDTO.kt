package com.growme.growme.data.model.quest


import com.google.gson.annotations.SerializedName

data class AddQuestRequestDTO(
    @SerializedName("contents")
    val contents: String,
    @SerializedName("exp")
    val exp: Int
)