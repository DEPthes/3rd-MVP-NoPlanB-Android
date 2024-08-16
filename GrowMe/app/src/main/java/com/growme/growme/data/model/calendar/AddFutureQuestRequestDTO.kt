package com.growme.growme.data.model.calendar


import com.google.gson.annotations.SerializedName

data class AddFutureQuestRequestDTO(
    @SerializedName("contents")
    val contents: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("exp")
    val exp: Int
)