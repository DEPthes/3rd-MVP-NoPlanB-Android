package com.growme.growme.data.model


import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: T?
)