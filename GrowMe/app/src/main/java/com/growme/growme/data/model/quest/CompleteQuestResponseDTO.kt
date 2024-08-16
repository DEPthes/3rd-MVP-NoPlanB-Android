package com.growme.growme.data.model.quest


import com.google.gson.annotations.SerializedName

data class CompleteQuestResponseDTO(
    @SerializedName("itemImageUrls")
    val itemImageUrls: List<String>,
    @SerializedName("questType")
    val questType: String
)