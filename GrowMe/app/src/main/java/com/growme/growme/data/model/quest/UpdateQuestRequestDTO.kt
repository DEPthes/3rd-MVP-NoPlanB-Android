package com.growme.growme.data.model.quest

import com.google.gson.annotations.SerializedName

data class UpdateQuestRequestDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("contents")
    val contents: String
)