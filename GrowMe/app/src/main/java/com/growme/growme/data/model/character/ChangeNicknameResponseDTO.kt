package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class ChangeNicknameResponseDTO(
    @SerializedName("message")
    val message: String
)