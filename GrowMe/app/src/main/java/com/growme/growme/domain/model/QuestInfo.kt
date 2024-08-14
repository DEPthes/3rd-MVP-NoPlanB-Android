package com.growme.growme.domain.model

import com.google.gson.annotations.SerializedName

data class QuestInfo(
    val id: Int,
    val contents: String,
    val exp: Int,
    var isComplete: Boolean
)