package com.growme.growme.data.model.character


import com.google.gson.annotations.SerializedName

data class ChangeNicknameRequestDTO(
    @SerializedName("newCharacterName")
    val newCharacterName: String
)