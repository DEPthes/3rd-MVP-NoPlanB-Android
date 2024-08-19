package com.growme.growme.data.model.quest


import com.google.gson.annotations.SerializedName

data class MainResponseDTO(
    @SerializedName("level")
    val level: Int,
    @SerializedName("acquireExp")
    val acquireExp: Int,
    @SerializedName("needExp")
    val needExp: Int,
    @SerializedName("todayExp")
    val todayExp: Int,
    @SerializedName("totQuestExp")
    val totQuestExp: Int
)