package com.growme.growme.data.model.quest


import com.google.gson.annotations.SerializedName

data class CUDQuestResponseDTO(
    @SerializedName("message")
    val message: String
)