package com.growme.growme.domain.model

import com.google.gson.annotations.SerializedName

data class HomeInfo(
    val level: Int,
    val acquireExp: Int,
    val needExp: Int,
    val todayExp: Int
)
