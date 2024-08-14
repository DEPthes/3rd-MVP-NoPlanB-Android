package com.growme.growme.domain.model.character

data class MyPageInfo(
    val characterName: String,
    val growDate: Int,
    val myCharaterDetailResList: List<MyCharacterDetailInfo>,
    val startDate: String,
    val totalExp: Int,
    val totalQuest: Int
)
