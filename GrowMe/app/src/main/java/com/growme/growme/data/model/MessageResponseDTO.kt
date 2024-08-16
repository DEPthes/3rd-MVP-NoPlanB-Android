package com.growme.growme.data.model


import com.google.gson.annotations.SerializedName

data class MessageResponseDTO(
    @SerializedName("message")
    val message: String
)