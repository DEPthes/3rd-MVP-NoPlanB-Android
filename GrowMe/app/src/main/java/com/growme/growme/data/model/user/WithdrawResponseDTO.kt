package com.growme.growme.data.model.user


import com.google.gson.annotations.SerializedName

data class WithdrawResponseDTO(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: String
)