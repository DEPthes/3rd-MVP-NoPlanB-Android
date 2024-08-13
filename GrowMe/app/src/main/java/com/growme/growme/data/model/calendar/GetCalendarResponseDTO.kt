package com.growme.growme.data.model.calendar


import com.google.gson.annotations.SerializedName

data class GetCalendarResponseDTO(
    @SerializedName("date")
    val date: String,
    @SerializedName("exp")
    val exp: Int
)