package com.growme.growme.domain.repository

import com.google.gson.annotations.SerializedName

data class CompleteQuestInfo(
    val itemImageUrls: List<String>,
    val questType: String
)
