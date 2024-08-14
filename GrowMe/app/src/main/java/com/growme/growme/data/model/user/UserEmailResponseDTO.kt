package com.growme.growme.data.model.user

import com.google.gson.annotations.SerializedName

data class UserEmailResponseDTO(
    @SerializedName("email")
    val email: String,
)
