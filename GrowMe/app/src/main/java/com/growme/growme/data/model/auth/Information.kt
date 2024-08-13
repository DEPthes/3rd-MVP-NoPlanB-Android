package com.growme.growme.data.model.auth


import com.google.gson.annotations.SerializedName

data class Information(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)