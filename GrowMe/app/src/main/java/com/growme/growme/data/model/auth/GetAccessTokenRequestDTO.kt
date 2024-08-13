package com.growme.growme.data.model.auth


import com.google.gson.annotations.SerializedName

data class GetAccessTokenRequestDTO(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("email")
    val email: String
)