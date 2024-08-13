package com.growme.growme.data.model.quest


import com.google.gson.annotations.SerializedName

data class QuestResponseDTO(
    @SerializedName("contents")
    val contents: String,
    @SerializedName("exp")
    val exp: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isComplete")
    val isComplete: Boolean
)