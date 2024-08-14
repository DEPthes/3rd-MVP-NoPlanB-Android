package com.growme.growme.domain.model.quest

data class QuestInfo(
    val id: Int,
    val contents: String,
    val exp: Int,
    var isComplete: Boolean
)