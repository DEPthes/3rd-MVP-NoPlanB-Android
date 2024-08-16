package com.growme.growme.data.model.calendar


import com.google.gson.annotations.SerializedName

data class GetMonthExpResponseDTOItem(
    @SerializedName("date")
    val date: String,
    @SerializedName("exp")
    val exp: Int
)