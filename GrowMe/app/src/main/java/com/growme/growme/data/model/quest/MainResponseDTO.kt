package com.growme.growme.data.model.quest


import com.google.gson.annotations.SerializedName

data class MainResponseDTO(
    @SerializedName("acquireExp")
    val acquireExp: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("needExp")
    val needExp: Int,
    @SerializedName("todayExp")
    val todayExp: Int
)