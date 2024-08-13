package com.growme.growme.data.model.auth


import com.google.gson.annotations.SerializedName

data class GetAccessTokenResponseDTO(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: Information
)