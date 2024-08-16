package com.growme.growme.domain.model.calendar


import com.google.gson.annotations.SerializedName

data class GetMonthExpInfoItem(
    val date: String,
    val exp: Int
)