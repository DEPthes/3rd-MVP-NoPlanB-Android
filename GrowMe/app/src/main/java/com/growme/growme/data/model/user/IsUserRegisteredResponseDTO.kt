package com.growme.growme.data.model.user


import com.google.gson.annotations.SerializedName

data class IsUserRegisteredResponseDTO(
    @SerializedName("exist")
    val exist: Boolean
)